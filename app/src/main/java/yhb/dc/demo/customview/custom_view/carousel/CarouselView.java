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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.NotNull;

import yhb.dc.R;

public class CarouselView extends RelativeLayout implements View.OnTouchListener {

    public static final String TAG = "CarouselView";

    private final static int PAGE_COUNT_RATIO = 10; // viewpager page count 翻倍倍数
    private final static int DOT_SIZE_DIP = 5;
    private final static int DOT_SELECTED_BG = R.drawable.carousel_indicator_selected;
    private final static int DOT_NORMAL_BG = R.drawable.carousel_indicator_normal;
    private final static int INTERVAL = 1 * 1000;
    private final static int DOT_MARGIN_HORIZONTAL_DIP = 3; // 圆点间距

    private HackViewPager mViewPager;
    private CarouselPagerAdapter mPagerAdapter;
    private LinearLayout mIndicator;
    private ImageView[] mDotViews = null;
    private Handler mHandler;

    private int mSelectedIndex = -1;
    private boolean mPlaying = false;
    private ViewProvider mViewProvider;
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
        View.inflate(getContext(), R.layout.view_carousel, this);
        mViewPager = findViewById(R.id.view_pager);
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
                int dataPosition = mSelectedIndex % mViewProvider.getCount();
                if (mDotViews == null || mDotViews.length == 0) {
                    return;
                }
                mDotViews[dataPosition].setBackgroundResource(DOT_SELECTED_BG);
                for (int i = 0; i < mDotViews.length; i++) {
                    if (dataPosition != i) {
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
                        Log.d(TAG, "mViewPager.setCurrentItem(" + resetPosition + ", false)");
                        mViewPager.setCurrentItem(resetPosition, false);
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
        // 保证 page 初始化页对应的 dataPosition 为 0
        int mid = PAGE_COUNT_RATIO * mViewProvider.getCount() / 2;
        if (mid % mViewProvider.getCount() != 0) {
            mid += (mViewProvider.getCount() - mid % mViewProvider.getCount());
        }
        return mid;
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
            mViewPager.setCurrentItem(getIntiPosition());
            mPagerAdapter.notifyDataSetChanged();
            layoutIndicator();
            if (oldPlaying) {
                startPlay();
            }
        }

    }

    private void layoutIndicator() {
        mIndicator.removeAllViews();
        boolean empty = mViewProvider == null || mViewProvider.getCount() <= 1;
        if (empty) {
            mIndicator.setVisibility(GONE);
            return;
        }
        int dotCount = mViewProvider.getCount();
        mDotViews = new ImageView[dotCount];
        mIndicator.setVisibility(VISIBLE);
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DOT_SIZE_DIP,
                getResources().getDisplayMetrics());

        int marginX = (int) (DOT_MARGIN_HORIZONTAL_DIP
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
            mDotViews[i] = dotView;
            mIndicator.addView(mDotViews[i], layoutParams);
        }
    }

    public interface ViewProvider {

        View createView(int position);

        int getCount();

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
            Log.d(TAG, "container.addView(view, view.getLayoutParams()) on pos " + position + " ->" + view);
            return view;
        }

        @Override
        public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            Log.d(TAG, "container.removeView((View) object) on pos " + position + " ->" + object);
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
                Log.d(TAG, "create view for position: " + dataPosition + ", now mCache.size=" + mCache.size() + ", mCache0.size=" + mCache0.size());
                mCache.put(dataPosition, view);
            } else if (view.getParent() != null) {
                // 此分支考虑两种情况：
                // 1. 只有两个 Page 时，当前现实的 Page 左边和右边从逻辑上来说，是同一个控件
                //    此时 instantiateItem， mCache 中取得的 View 已经被使用;
                // 2. 当滑动到 ViewPager 的最后一个页面（极限情况，滑动了 1000*count 页） 时，需要重置到 init position
                //    此时 instantiateItem，mCache 中取得的 View 已经被使用。
                // 对这两种情况需要重新 createView() 生成另一个实例
                // mCache0 用于对冲突实例的缓存
                view = mCache0.get(dataPosition);
                if (view != null && view.getParent() == null) {
                    Log.d(TAG, "found view from mCache0 for position: " + dataPosition + ", now mCache.size=" + mCache.size() + ", mCache0.size=" + mCache0.size());
                    return view;
                } else if (view == null) {
                    view = mViewProvider.createView(dataPosition);
                    mCache0.put(dataPosition, view);
                    Log.d(TAG, "create view for position: " + dataPosition + ", now mCache.size=" + mCache.size() + ", mCache0.size=" + mCache0.size());
                } else {
                    // 此分支考虑上述两种情况同时成立时，mCache、mCache0 中两个缓存实例已经同时被使用了
                    // 此时 instantiateItem，需要第三个实例，对该实例不进行 Cache
                    Log.d(TAG, "create view for position: " + dataPosition + ", now mCache.size=" + mCache.size() + ", mCache0.size=" + mCache0.size());
                    return mViewProvider.createView(dataPosition);
                }
            } else {
                Log.d(TAG, "found view from mCache for position: " + dataPosition + ", now mCache.size=" + mCache.size() + ", mCache0.size=" + mCache0.size());
            }
            return view;
        }

        void clearViewCache() {
            mCache.clear();
            mCache0.clear();
        }
    }


}
