package yhb.dc.demo.customview.custom_view.obselete;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class HackViewPager extends ViewPager {

    private boolean isPagingEnabled = true;
    private boolean isAnimateSwitchEnable = true;
    private boolean isNestedPagingEnable = false;

    public HackViewPager(Context context) {
        this(context, null);
    }

    public HackViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isPagingEnabled && !isNestedPagingEnable) {
            return false;
        }
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isPagingEnabled && !isNestedPagingEnable) {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // 启用/禁用 viewPager滑动切换
    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

    // 切换tab动画特效
    @SuppressWarnings("unused")
    public void setAnimateSwitchEnable(boolean b) {
        this.isAnimateSwitchEnable = b;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return isPagingEnabled && super.canScrollHorizontally(direction);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, isAnimateSwitchEnable);
    }
}