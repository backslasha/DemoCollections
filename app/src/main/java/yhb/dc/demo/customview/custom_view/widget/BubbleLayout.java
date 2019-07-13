package yhb.dc.demo.customview.custom_view.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;

/**
 * Created by yhb on 18-7-21.
 */

public class BubbleLayout extends ViewGroup {

    private BubbleProvider mBubbleProvider;
    private int maxChildWidth = Integer.MIN_VALUE, maxChildHeight = Integer.MIN_VALUE;
    private int minChildWidth = Integer.MAX_VALUE;
    private int minChildHeight = Integer.MAX_VALUE;

    private int scanStep = 3;

    public BubbleLayout(Context context) {
        this(context, null);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBubbleProvider = new BubbleProviderImpl();
        fillBubbles();
        setupAnimation();
        scanStep = dp(3,context);
    }

    private static int dp(int x, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                x, context.getResources().getDisplayMetrics());
    }


    private void setupAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.bubble_show);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
        controller.setInterpolator(new OvershootInterpolator());
        controller.setDelay(0.1f);
        setLayoutAnimation(controller);
    }

    private void fillBubbles() {
        int count = mBubbleProvider.getCount();
        for (int i = 0; i < count; i++) {
            View bubble = mBubbleProvider.getView(mBubbleProvider.nextBubble(), getContext());
            bubble.setId(mBubbleProvider.getId(i));
            addView(bubble);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        int totalChildrenWidth = 0;
        for (int i = 0; i < childCount; i++) {
            int measuredHeight = getChildAt(i).getMeasuredHeight();
            int measuredWidth = getChildAt(i).getMeasuredWidth();
            maxChildHeight = measuredHeight > maxChildHeight ? measuredHeight : maxChildHeight;
            minChildHeight = measuredHeight < minChildHeight ? measuredHeight : minChildHeight;
            maxChildWidth = measuredWidth > maxChildWidth ? measuredWidth : maxChildWidth;
            minChildWidth = measuredWidth < minChildWidth ? measuredWidth : minChildWidth;
            totalChildrenWidth += measuredWidth;
        }

        int resultWidth, resultHeight;
        int room = maxChildHeight * totalChildrenWidth;

        // 高度必须能容纳单个气泡
        resultHeight = Math.max(sizeHeight, maxChildHeight);
        resultWidth = room / resultHeight + maxChildWidth;

        setMeasuredDimension(resultWidth, resultHeight); // 由于布局 width 目前是不确定的，因此在 onLayout 需要进行调整
    }

    private final List<View> mBufferJar = new ArrayList<>();
    private final Point mBufferPoint = new Point();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mBufferJar.clear();
        x = maxChildWidth / 2;
        y = maxChildHeight / 2;
        int childCount = getChildCount();
        int borderRight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            Point point = availableCenter(childView.getMeasuredHeight() / 2);
            int left = point.x - childView.getMeasuredWidth() / 2;
            int top = point.y - childView.getMeasuredHeight() / 2;
            int right = point.x + childView.getMeasuredWidth() / 2;
            int bottom = point.y + childView.getMeasuredHeight() / 2;
            childView.layout(left, top, right, bottom);
            if (right > borderRight) {
                borderRight = right;
            }
            mBufferJar.add(childView);
        }

        // 布局后调整控件宽度
        int offset = borderRight - r;
        if (offset != 0) {
            setMeasuredDimension(getMeasuredWidth() + offset, getMeasuredHeight());
            layout(l, t, r + offset, b);
        }
    }

    @VisibleForTesting
    public void showBubbleAnim() {
        startLayoutAnimation();
    }

    @VisibleForTesting
    public void repaint() {
        removeAllViews();
        mBubbleProvider.reset();
        fillBubbles();
        invalidate();
    }

    @VisibleForTesting
    public void doubleBubbles() {
        ((BubbleProviderImpl) mBubbleProvider).doubleBubbles();
        fillBubbles();
        invalidate();
    }



    /*------------------ 辅助计算 -------------------------------*/

    private int x = 0, y = 0;

    private Point availableCenter(int radius) {
        while (true) {
            if (y >= getMeasuredHeight() - maxChildHeight / 2) {
                x += scanStep;
                y = maxChildHeight / 2;
            }
            if (checkOverlap(x, y, radius)) {
                y += scanStep;
            } else {
                mBufferPoint.set(x, y);
                return mBufferPoint;
            }
        }
    }

    private boolean checkOverlap(int x, int y, int radius) {
        Rect rect = new Rect(x - radius, y - radius, x + radius, y + radius);
        if (mBufferJar.isEmpty()) return false;
        int maxChildCountVertical = getMeasuredHeight() / minChildHeight;
        for (int i = mBufferJar.size() - 1; i >= Math.max(mBufferJar.size() - maxChildCountVertical * 2 - 1, 0); i--) {
            View other = mBufferJar.get(i);
            if (checkOverlap(rect, other)) return true;
        }
        return false;
    }

    private boolean checkOverlap(Rect rect, View child) {
        if (child == null || rect == null) {
            return false;
        }
        Rect rect0 = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());

        float dx = rect0.centerX() - rect.centerX();
        float dy = rect0.centerY() - rect.centerY();
        float r = rect0.height() / 2 + rect.height() / 2;
        float d = (dx * dx) + (dy * dy);

        return d < r * r;
    }

    /*---------------------- 提供数据 ---------------------------------*/


    public interface BubbleProvider {

        int getCount();

        Bubble nextBubble();

        Bubble askSmaller(Bubble bubble);

        void reset();

        View getView(Bubble bubble, Context context);

        int getId(int index);
    }

    static class Bubble {
        boolean selected;
        int level;
        String label;
        int textSize;
        int selectedTextColor;

        Bubble(boolean selected, int level, String label, int textSize, int selectedTextColor, int unselectedTextColor, int selectedRes, int unselectedRes) {
            this.selected = selected;
            this.level = level;
            this.label = label;
            this.textSize = textSize;
            this.selectedTextColor = selectedTextColor;
            this.unselectedTextColor = unselectedTextColor;
            this.selectedRes = selectedRes;
            this.unselectedRes = unselectedRes;
        }

        int unselectedTextColor;
        int selectedRes;
        int unselectedRes;


    }

    static class BubbleGroup {

        int level;
        List<Bubble> bubbles;
        float ratio1, ratio2;

        BubbleGroup(int level, List<Bubble> bubbles, int currentTotalBubbleCount) {
            this.level = level;
            this.bubbles = bubbles;
            this.ratio1 = ((float) bubbles.size()) / ((float) currentTotalBubbleCount);
            this.ratio2 = ratio1;
        }

        void updateRatio(int currentTotalBubbleCount) {
            ratio2 = ratio1;
            ratio1 = ((float) bubbles.size()) / ((float) currentTotalBubbleCount);
        }

        float getRatioIncreaseSpeed() {
            return ratio1 - ratio2;
        }

    }
}

