package yhb.dc.demo.customview.custom_view.carousel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.NotNull;

import yhb.dc.R;


/*
 * 参照{@link sg.bigo.live.explore.BannerPageView} 实现的去掉业务耦合版本的自动轮播控件
 */
public class CarouselView extends RelativeLayout implements View.OnTouchListener {

    public static final String TAG = "CarouselView";

    private final static int DOT_SIZE_DIP = 5;
    private final static int DOT_SELECTED_BG = R.drawable.carousel_indicator_selected;
    private final static int DOT_NORMAL_BG = R.drawable.carousel_indicator_normal;
    private final static int INTERVAL = 1 * 1000;

    private HackViewPager mViewPager;
    private CarouselPagerAdapter mPagerAdapter;
    private LinearLayout mIndicator;
    private ImageView[] mDotViews = null;
    private Handler mHandler;

    private float mDotSize = 5;
    private float mDotMarginTop = 0;
    private float mDotMarginBottom = 0;
    private float mDotMarginLeft = 0;
    private float mDotMarginRight = 0;

    private int mTargetIndex = 0;
    private int mSelectedIndex = -1;
    private boolean mPlaying = false;
    private ViewProvider mViewProvider;
    private Runnable mPageRunner = new Runnable() {
        @Override
        public void run() {
            int count = mViewProvider.getCount();
            if (count > 1 && mTargetIndex < count && mTargetIndex >= 0) {
                mViewPager.setCurrentItem(mTargetIndex, true);
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
        readAttrs(context, attrs);
        setupView();
    }

    public float getDotSize() {
        return mDotSize;
    }

    public void setDotSize(float dotSize) {
        mDotSize = dotSize;
    }

    public float getDotMarginTop() {
        return mDotMarginTop;
    }

    public void setDotMarginTop(float dotMarginTop) {
        mDotMarginTop = dotMarginTop;
    }

    public float getDotMarginBottom() {
        return mDotMarginBottom;
    }

    public void setDotMarginBottom(float dotMarginBottom) {
        mDotMarginBottom = dotMarginBottom;
    }

    public float getDotMarginLeft() {
        return mDotMarginLeft;
    }

    public void setDotMarginLeft(float dotMarginLeft) {
        mDotMarginLeft = dotMarginLeft;
    }

    public float getDotMarginRight() {
        return mDotMarginRight;
    }

    public void setDotMarginRight(float dotMarginRight) {
        mDotMarginRight = dotMarginRight;
    }

    private void readAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CarouselView);
//        mDotSize = ta.getDimension(R.styleable.CarouselView_dot_size, 0);
//        mDotMarginTop = ta.getDimension(R.styleable.CarouselView_dot_margin_top, 0);
//        mDotMarginBottom = ta.getDimension(R.styleable.CarouselView_dot_margin_bottom, 0);
//        mDotMarginLeft = ta.getDimension(R.styleable.CarouselView_dot_margin_left, 0);
//        mDotMarginRight = ta.getDimension(R.styleable.CarouselView_dot_margin_right, 0);
//        ta.recycle();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupView() {
        View.inflate(getContext(), R.layout.view_carousel, this);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        mIndicator = findViewById(R.id.indicator_container);
        mHandler = new Handler(Looper.getMainLooper());
        mViewPager.setOnTouchListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mSelectedIndex = position;
                if (mViewProvider == null || mViewProvider.getCount() == 0) {
                    return;
                }
                if (mDotViews == null || mDotViews.length == 0) {
                    return;
                }
                mDotViews[position].setBackgroundResource(DOT_SELECTED_BG);
                for (int i = 0; i < mDotViews.length; i++) {
                    if (position != i) {
                        mDotViews[i].setBackgroundResource(DOT_NORMAL_BG);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (mPlaying) {
                        mTargetIndex = (mSelectedIndex + 1) % mViewProvider.getCount();
                        startCountdown();
                    }
                }
            }
        });
    }

    public void startPlay() {
        if (mViewProvider == null) {
            return;
        }
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
        if (mViewProvider == null) {
            return;
        }
        cancelCountdown();
        if (mViewProvider.getCount() > 1) {
            mTargetIndex = (mViewPager.getCurrentItem() + 1) % mViewProvider.getCount();
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

    public void setViewProvider(ViewProvider viewProvider) {
        if (viewProvider == null) {
            mViewProvider = null;
            stopPlay();
            mPagerAdapter.notifyDataSetChanged();
            mViewPager.setPagingEnabled(false);
            layoutIndicator();
        } else if (mViewProvider == viewProvider) {
            mPagerAdapter.notifyDataSetChanged();
            layoutIndicator();
        } else {
            mViewProvider = viewProvider;
            if (mPagerAdapter == null) {
                mPagerAdapter = new CarouselPagerAdapter();
            }
            final boolean oldPlaying = mPlaying;
            stopPlay();
            mPagerAdapter.clearViewCache();
            mViewPager.setPagingEnabled(true);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            mPagerAdapter.notifyDataSetChanged();
            layoutIndicator();
            if (oldPlaying) {
                startPlay();
            }
        }

    }

    public void layoutIndicator() {
        mIndicator.removeAllViews();
        boolean empty = mViewProvider == null || mViewProvider.getCount() <= 1;
        if (empty) {
            mIndicator.setVisibility(GONE);
            return;
        }
        int dotCount = mViewProvider.getCount();
        mDotViews = new ImageView[dotCount];
        mIndicator.setVisibility(VISIBLE);

        RelativeLayout.LayoutParams indicatorLp = (LayoutParams) mIndicator.getLayoutParams();
        indicatorLp.topMargin = (int) mDotMarginTop;
        mIndicator.setLayoutParams(indicatorLp);

        for (int i = 0; i < dotCount; i++) {
            ImageView dotView;
            LinearLayout.LayoutParams layoutParams
                    = new LinearLayout.LayoutParams((int) mDotSize, (int) mDotSize);
            layoutParams.leftMargin = (int) mDotMarginLeft;
            layoutParams.rightMargin = (int) mDotMarginRight;
            layoutParams.width = layoutParams.height = (int) mDotSize;
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
            mDotViews[i] = dotView;
            mIndicator.addView(mDotViews[i], layoutParams);
        }
    }

    /**
     * 刷新布局
     */
    public interface ViewProvider {

        View createView(int position);

        int getCount();

    }

    private class CarouselPagerAdapter extends PagerAdapter {

        private SparseArray<View> mCache = new SparseArray<>();

        @Override
        public int getCount() {
            if (mViewProvider == null) {
                return 0;
            }
            return mViewProvider.getCount();
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            View view = getOrCreateByPosition(position);
            try {
                if (view.getParent() != null) {
                    ((ViewGroup) view.getParent()).removeView(view);
//                    sg.bigo.log.Log.d(TAG, "mViewProvider.getCount()=" + mViewProvider.getCount()
//                            + ", Page position=" + position + ", data position=" + dataPosition
//                            + ", mCache.size()=" + mCache.size() + ", mCache0.size()=" + mCache0.size());
                }
                container.addView(view, view.getLayoutParams());

            } catch (IllegalStateException e) {
//                sg.bigo.log.Log.e(TAG, e.toString());
                view = mViewProvider.createView(position);
            }
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

        private View getOrCreateByPosition(int position) {
            View view = mCache.get(position);
            if (view == null) {
                view = mViewProvider.createView(position);
                mCache.put(position, view);
            }
            return view;
        }

        void clearViewCache() {
            mCache.clear();
        }
    }


}
