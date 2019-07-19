package yhb.dc.demo.customview.custom_view.carousel;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import yhb.dc.R;

public class CarouselViewPager extends ViewPager {

    private static final long INTERVAL = 1000L;
    private final static int DOT_SELECTED_BG = R.drawable.banner_indicator_selected;
    private final static int DOT_NORMAL_BG = R.drawable.banner_indicator_normal;
    private static final int DOT_TOP_MARGIN_VERTICAL = 100;
    private static final int DOT_BOTTOM_MARGIN_VERTICAL = 100;
    private static final float DOT_SIZE_DIP = 12;
    private static final int DOT_MARGIN_HORIZONTAL = 100;
    private final Handler mHandler;
    private int mSelectedIndex = -1;
    private CarouselPagerAdapter mCarouselAdapter;
    private ImageView[] mDotViews = null;
    private LinearLayout mIndicator;
    private Runnable mTimerRunner = new Runnable() {
        @Override
        public void run() {
            if (mDotViews != null) {
                if (mSelectedIndex == mCarouselAdapter.getCount() - 1) {
                    setCurrentItem(0, false);
                    startCountdown();
                } else {
                    setCurrentItem(mSelectedIndex + 1, true);
                }
            }
        }
    };
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mSelectedIndex = position;
            int nextIndex = mSelectedIndex % mCarouselAdapter.getCount();
            if (mDotViews == null || mDotViews.length == 0) {
                return;
            }
            // 设置图片滚动指示器背景
            mDotViews[nextIndex].setBackgroundResource(DOT_SELECTED_BG);
            for (int i = 0; i < mDotViews.length; i++) {
                if (nextIndex != i) {
                    mDotViews[i].setBackgroundResource(DOT_NORMAL_BG);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                startCountdown(); // 开始下次计时
            }
        }
    };

    public CarouselViewPager(Context context) {
        this(context, null);
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler(Looper.getMainLooper());
    }

    private void layoutIndicator() {
        mIndicator.removeAllViews();
        if (mCarouselAdapter.getCount() <= 1) {
            mDotViews = null;
            return;
        }
        mDotViews = new ImageView[mCarouselAdapter.getCount()];
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DOT_SIZE_DIP,
                getResources().getDisplayMetrics());
        for (int i = 0; i < mCarouselAdapter.getCount(); i++) {
            ImageView dotView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) size, (int) size);
            layoutParams.leftMargin = DOT_MARGIN_HORIZONTAL;
            layoutParams.rightMargin = DOT_MARGIN_HORIZONTAL;

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
            if (i == mCarouselAdapter.getCount() - 1) {
                layoutParams.rightMargin = 0;
            }
            layoutParams.topMargin = DOT_TOP_MARGIN_VERTICAL;
            layoutParams.bottomMargin = DOT_BOTTOM_MARGIN_VERTICAL;
            mDotViews[i] = dotView;
            mIndicator.addView(mDotViews[i], layoutParams);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {

    }

    public void setCarouselController(CarouselController carouselController) {
        super.setAdapter(mCarouselAdapter = new CarouselPagerAdapter(carouselController));
    }

    public void startCountdown() {
        stopCountdown();
        if (mCarouselAdapter.getCount() > 1) {
            mHandler.postDelayed(mTimerRunner, INTERVAL);
        }
    }

    public void stopCountdown() {
        mHandler.removeCallbacks(mTimerRunner);
    }

    public interface CarouselController {

        int getCount();

        View createView(ViewGroup container, int position);
    }

    private class CarouselPagerAdapter extends PagerAdapter {

        private CarouselController mCarouselController;

        CarouselPagerAdapter(CarouselController carouselController) {
            mCarouselController = carouselController;
        }

        @Override
        public int getCount() {
            return mCarouselController.getCount();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrameLayout fl = new FrameLayout(container.getContext());
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.CENTER;
            fl.setBackgroundColor(Color.BLUE);
            fl.addView(mCarouselController.createView(container, position), lp);
            container.addView(fl);
            return fl;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


}
