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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import yhb.dc.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class CarouselViewV0 extends RelativeLayout implements View.OnTouchListener {

    public static final String TAG = "CarouselView";

    private final static int PAGE_COUNT_RATIO = 10000; // viewpager page count 翻倍倍数
    private final static int DOT_SIZE_DIP = 5;
    private final static int DOT_SELECTED_BG = R.drawable.carousel_indicator_selected;
    private final static int DOT_NORMAL_BG = R.drawable.carousel_indicator_normal;
    private final static int INTERVAL = 5 * 1000;
    private final static int DOT_MARGIN_HORIZONTAL_DIP = 3; // 圆点间距

    private HackViewPager mViewPager;
    private CarouselPagerAdapter mPagerAdapter;
    private LinearLayout mIndicator;
    private ImageView[] mDotViews = null;
    private Handler mHandler;
    // 1080
    // 2029

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

    public CarouselViewV0(@NonNull Context context) {
        this(context, null);
    }

    public CarouselViewV0(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselViewV0(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
        final float screenWidth = getResources().getDisplayMetrics().widthPixels;
        final float screenHeight = getResources().getDisplayMetrics().heightPixels;
        Log.d(TAG, "Screen Width=" + screenWidth + "dpi");
        Log.d(TAG, "Screen Height" + screenHeight + "dpi");
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
                Log.d(TAG, "[Carousel.state] onPageSelected, mSelectedIndex=" + position);
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
                    firstPosOnDrag = -1;
                    Log.d(TAG, "[Carousel.state] onPageScrollStateChanged, state=" + state + ", firstPosOnDrag= " + firstPosOnDrag);
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    firstPosOnDrag = -1;
                    Log.d(TAG, "[Carousel.state] onPageScrollStateChanged, state=" + state + ", firstPosOnDrag= " + firstPosOnDrag);

                }
            }

            private boolean toNextDone = false;
            private boolean toPrevDone = false;
            private int firstPosOnDrag = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.d(TAG, "[Carousel.onPageScrolled] = " + position + ", " + positionOffset + ", " + positionOffsetPixels);

                if(position != firstPosOnDrag){ // 方向改变
                    toPrevDone = toNextDone = false;
                    Log.d(TAG, "[Carousel.onPageScrolled] position=" + position + " firstPosOnDrag=" + firstPosOnDrag + ", toPrevDone = toNextDone = false");
                }

                if (toPrevDone || toNextDone) {
                    return;
                }

                final boolean drag2Next = (position == mSelectedIndex) && positionOffsetPixels > 0;
                if (drag2Next) {
                    onToNext(position);// 手向左滑动
                    toNextDone = true;
                    firstPosOnDrag = position;
                    return;
                }

                final boolean drag2Prev = (position == mSelectedIndex - 1) && positionOffsetPixels > 0;
                if (drag2Prev) {
                    onToPrev(position);// 手向右滑动
                    toPrevDone = true;
                    firstPosOnDrag = position;
                }

            }
        });
    }

    private void onToPrev(int position) {
        int dataPosition = position % mViewProvider.getCount();
        TextView content = (TextView) mPagerAdapter.getOrCreateByDataPosition(dataPosition);
        if (content.getParent() != null) {
            ((ViewGroup) content.getParent()).removeView(content);
        }
        View fl = mViewPager.findViewWithTag(position);
        FrameLayout childAt = (FrameLayout) fl;
        childAt.removeAllViews();
        childAt.addView(content);

        Log.d(TAG, "[Carousel.interface] 上一页露出来");
        Log.d(TAG, "[Carousel.addView] ----------------------------------------------------------------------------------------------------");
        Log.d(TAG, "[Carousel.addView] add View" + content.getText() + " to fl0:" + fl.getLeft() + "," + fl.getRight());
        Log.d(TAG, "[Carousel.addView] f0 -> " + mViewPager.findViewWithTag(position).getLeft() + ", " + mViewPager.findViewWithTag(position).getRight());
        Log.d(TAG, "[Carousel.addView] f1 -> " + mViewPager.findViewWithTag(position + 1).getLeft() + ", " + mViewPager.findViewWithTag(position + 1).getRight());
        Log.d(TAG, "[Carousel.addView] f2 -> " + mViewPager.findViewWithTag(position + 2).getLeft() + ", " + mViewPager.findViewWithTag(position + 2).getRight());
        Log.d(TAG, "[Carousel.addView] ----------------------------------------------------------------------------------------------------");

        Toast.makeText(getContext(),
                "add TextView(" + content.getText() + ") to mViewPager.findViewWithTag(0):" + fl, Toast.LENGTH_SHORT).show();
    }

    private void onToNext(int position) {
        int dataPosition = (position + 1) % mViewProvider.getCount();
        TextView content = (TextView) mPagerAdapter.getOrCreateByDataPosition(dataPosition);
        if (content.getParent() != null) {
            ((ViewGroup) content.getParent()).removeView(content);
        }
        FrameLayout fl = mViewPager.findViewWithTag(position + 1);
        fl.addView(content);
        Log.d(TAG, "[Carousel.interface] 下一页露出来");
        Log.d(TAG, "[Carousel.addView] ----------------------------------------------------------------------------------------------------");
        Log.d(TAG, "[Carousel.addView] add TextView" + content.getText() + " to fl2:" + fl.getLeft() + "," + fl.getRight());
        Log.d(TAG, "[Carousel.addView] fl0 -> " + mViewPager.findViewWithTag(position - 1).getLeft() + ", " + mViewPager.findViewWithTag(position - 1).getRight());
        Log.d(TAG, "[Carousel.addView] fl1 -> " + mViewPager.findViewWithTag(position).getLeft() + ", " + mViewPager.findViewWithTag(position).getRight());
        Log.d(TAG, "[Carousel.addView] fl2 -> " + mViewPager.findViewWithTag(position + 1).getLeft() + ", " + mViewPager.findViewWithTag(position + 1).getRight());
        Log.d(TAG, "[Carousel.addView] ----------------------------------------------------------------------------------------------------");

        Toast.makeText(getContext(),
                "add TextView(" + content.getText() + ") to mViewPager.findViewWithTag(2):" + fl, Toast.LENGTH_SHORT).show();
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
            View view = new FrameLayout(container.getContext());
            view.setTag(position);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(0, 0);
            }
            layoutParams.height = MATCH_PARENT;
            layoutParams.width = MATCH_PARENT;
            container.addView(view, layoutParams);
            Log.d(TAG, "[Carousel.addView] container.addView(view, view.getLayoutParams()) on pos " + position + " ->" + view);
            return view;
        }

        @Override
        public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            Log.d(TAG, "[Carousel.addView] container.removeView((View) object) on pos " + position + " ->" + object);
            ((View) object).setTag(-1);
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

        void clearViewCache() {
            mCache.clear();
        }

        View getOrCreateByDataPosition(int dataPosition) {
            View view = mCache.get(dataPosition);
            if (view == null) {
                view = mViewProvider.createView(dataPosition);
                Log.d(TAG, "create view for position: " + dataPosition + ", now mCache.size=" + mCache.size());
                mCache.put(dataPosition, view);
            }
            return view;
        }
    }


}
