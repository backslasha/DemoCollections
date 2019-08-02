package yhb.dc.demo.customview.custom_view.carousel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.jetbrains.annotations.NotNull;

import yhb.dc.R;


public class CarouselView extends FrameLayout implements View.OnTouchListener {

    public static final String TAG = "CarouselView";

    private final static int DOT_SIZE_DIP = 6;
    private final static int DOT_SELECTED_BG = R.drawable.banner_indicator_selected;
    private final static int DOT_NORMAL_BG = R.drawable.banner_indicator_normal;
    private final static int PAGE_COUNT_RATIO = 1000; // viewpager page count 翻倍倍数
    private final static int INTERVAL = 2000;
    private final static int DOT_MARGIN_HORIZONTAL_DIP = 3; // 圆点左右间距
    private final static int DOT_MARGIN_VERTICAL_DIP = 5; // 圆点上下间距

    private HackViewPager mViewPager;
    private CarouselPagerAdapter mPagerAdapter;
    private LinearLayout mIndicator;
    private ImageView[] mDotViews = null;
    private Handler mHandler;

    private int mSelectedIndex = -1;
    private boolean mPlaying = false;
    private ViewProvider mViewProvider;
    private OnItemSelectedListener mOnItemSelectedListener;
    private Runnable mPageRunner = new Runnable() {
        @Override
        public void run() {
            if (mViewProvider.getCount() > 1) {
                mViewPager.setCurrentItem(mSelectedIndex + 1, true);
            }
        }
    };

    public CarouselView(@NonNull Context context) {
        this(context, null);
    }

    public CarouselView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupView() {
        View.inflate(getContext(), R.layout.view_banner_page, this);
        mViewPager = findViewById(R.id.view_pager);
        mIndicator = findViewById(R.id.indicator_container);
        mHandler = new Handler(Looper.getMainLooper());
        mViewPager.setOnTouchListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mSelectedIndex = position;
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(
                            mPagerAdapter.getDisplayViewByDataPosition(mSelectedIndex % mViewProvider.getCount()), mSelectedIndex % mViewProvider.getCount());
                }
                syncIndicators(mSelectedIndex % mViewProvider.getCount());
            }

            private void syncIndicators(int index) {
                if (mDotViews == null || mDotViews.length == 0) {
                    return;
                }
                mDotViews[index].setBackgroundResource(DOT_SELECTED_BG);
                for (int i = 0; i < mDotViews.length; i++) {
                    if (index != i) {
                        mDotViews[i].setBackgroundResource(DOT_NORMAL_BG);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (mPlaying) {
                        startCountdown();
                    }
                    // 处理 viewpager 滑动到边界的情况
                    if (mSelectedIndex == mPagerAdapter.getCount() - 1) {
                        int dataPosition = mSelectedIndex % mViewProvider.getCount();
                        int resetPosition = getIntiPosition() + dataPosition;
                        mViewPager.setCurrentItem(resetPosition, false);
                        Log.d(TAG, "reset position from " + mSelectedIndex + " to position " +
                                (getIntiPosition() + dataPosition) + " without animation.");
                        mSelectedIndex = resetPosition;
                    }
                }
            }
        });
    }

    public void startPlay() {
        if (mViewProvider.getCount() > 1) {
            startCountdown();
            mPlaying = true;
        }
    }

    public void stopPlay() {
        cancelCountdown();
        mPlaying = false;
    }

    public void clearViewCache() {
        if (mPagerAdapter != null) {
            mPagerAdapter.clearViewCache();
        }
    }

    private void startCountdown() {
        cancelCountdown();
        if (mViewProvider.getCount() > 1) {
            mHandler.postDelayed(mPageRunner, INTERVAL);
        }
    }

    private void cancelCountdown() {
        mHandler.removeCallbacks(mPageRunner);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!mPlaying) return false;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            startCountdown();
        } else {
            cancelCountdown(); // 用户手动滑动 ViewPager 时停止定时轮播
        }
        return false;
    }

    private int getIntiPosition() {
        if (mViewProvider.getCount() == 0) {
            return 0;
        }
        return PAGE_COUNT_RATIO / 2 * mViewProvider.getCount();
    }

    public void setViewProvider(ViewProvider viewProvider) {
        mViewProvider = viewProvider;
        if (viewProvider == null) {
            stopPlay();
            mPagerAdapter.clearViewCache();
            mPagerAdapter.notifyDataSetChanged();
            mViewPager.setPagingEnabled(false);
        } else {
            if (mPagerAdapter == null) {
                mPagerAdapter = new CarouselPagerAdapter();
            }
            mViewPager.setPagingEnabled(true);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(getIntiPosition());
            mPagerAdapter.notifyDataSetChanged();
            layoutIndicator();
        }

    }

    private void layoutIndicator() {
        mIndicator.removeAllViews();
        int dotCount = mViewProvider.getCount();
        mDotViews = new ImageView[dotCount];
        if (dotCount <= 1) {
            mIndicator.setVisibility(GONE);
            return;
        }
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DOT_SIZE_DIP,
                getResources().getDisplayMetrics());

        int marginX = (int) (DOT_MARGIN_HORIZONTAL_DIP
                * Resources.getSystem().getDisplayMetrics().density);
        int marginY = (int) (DOT_MARGIN_VERTICAL_DIP
                * Resources.getSystem().getDisplayMetrics().density);

        for (int i = 0; i < dotCount; i++) {
            ImageView dotView;
            LinearLayout.LayoutParams layoutParams
                    = new LinearLayout.LayoutParams((int) size, (int) size);
            layoutParams.leftMargin = marginX;
            layoutParams.rightMargin = marginX;
            layoutParams.width = layoutParams.height = (int) size;
            dotView = new ImageView(getContext());
            if (i == 0) {
                dotView.setBackgroundResource(DOT_SELECTED_BG);
            } else {
                dotView.setBackgroundResource(DOT_NORMAL_BG);
            }
            if (i == 0) {
                layoutParams.leftMargin = 0;
            }
            if (i == dotCount - 1) {
                layoutParams.rightMargin = 0;
            }
            layoutParams.topMargin = marginY;
            layoutParams.bottomMargin = marginY;
            mDotViews[i] = dotView;
            mIndicator.addView(mDotViews[i], layoutParams);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public interface ViewProvider {

        View createView(int position);

        int getCount();

    }

    public interface OnItemSelectedListener {
        void onItemSelected(View view, int position);
    }

    private class CarouselPagerAdapter extends PagerAdapter {

        private SparseArray<View> mCache = new SparseArray<>();
        private SparseArray<View> mCache0 = new SparseArray<>();

        @Override
        public int getCount() {
            if (mViewProvider == null) {
                return 0;
            }
            int size = mViewProvider.getCount();
            if (size > 1) {
                return PAGE_COUNT_RATIO * size;
            } else {
                return size;
            }
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            int dataPosition = position % mViewProvider.getCount();
            View view = getOrCreateByDataPosition(dataPosition);
            container.addView(view, view.getLayoutParams());
            return view;
        }

        @Override
        public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NotNull Object object) {
            // Causes adapter to reload all Views when
            // notifyDataSetChanged is called
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
            return view == object;
        }

        private View getOrCreateByDataPosition(int dataPosition) {
            View view = mCache.get(dataPosition);
            if (view == null) {
                view = mViewProvider.createView(dataPosition);
                mCache.put(dataPosition, view);
            } else if (view.getParent() != null) {
                view = mCache0.get(dataPosition);
                if (view != null) {
                    return view;
                } else {
                    view = mViewProvider.createView(dataPosition);
                    mCache0.put(dataPosition, view);
                }
            }
            return view;
        }

        void clearViewCache() {
            mCache.clear();
            mCache0.clear();
        }

        private View getDisplayViewByDataPosition(int dataPosition) {
            View view = mCache.get(dataPosition);
            if (view == null || view.getParent() == null) {
                view = mCache0.get(dataPosition);
            }
            return view;
        }
    }


}
