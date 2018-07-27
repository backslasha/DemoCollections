package yhb.dc.demo.demo_view.custom_view.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import yhb.dc.R;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * Created by yhb on 18-7-21.
 */

public class BubbleLayout1 extends ViewGroup {


    private int maxChildHeight;
    private int minChildHeight;
    private int maxChildWidth;
    private int minChildWidth;
    private LayoutHelper layoutHelper;
    private Map<LayoutHelper.Bubble, View> mViewBubbleMap;

    public BubbleLayout1(Context context) {
        this(context, null);
    }

    public BubbleLayout1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAnimation();
        mViewBubbleMap = new HashMap<>();
        layoutHelper = new LayoutHelper(1280 - 350 * 2, new BubbleProviderImpl1(getContext()));
        bubbleRectLinkedHashMap = layoutHelper.doJob();
        fillBubbles();
    }

    private void fillBubbles() {
        if (bubbleRectLinkedHashMap == null) {
            return;
        }

        Set<LayoutHelper.Bubble> bubbles = bubbleRectLinkedHashMap.keySet();
        for (LayoutHelper.Bubble bubble : bubbles) {
            View view = layoutHelper.getView(bubble, getContext());
            addView(view);
            mViewBubbleMap.put(bubble, view);
        }
    }

    private void setupAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.bubble_show);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
        controller.setInterpolator(new OvershootInterpolator());
        controller.setDelay(0.1f);
        setLayoutAnimation(controller);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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

        resultHeight = Math.max(maxChildHeight, sizeHeight);

        int room = maxChildHeight * totalChildrenWidth;

        resultWidth = room / resultHeight + maxChildWidth;

        setMeasuredDimension(resultWidth, resultHeight); // 由于布局 width 目前是不确定的，因此在 onLayout 需要进行调整

    }

    LinkedHashMap<LayoutHelper.Bubble, Rect> bubbleRectLinkedHashMap;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (bubbleRectLinkedHashMap == null) {
            return;
        }
        Set<LayoutHelper.Bubble> bubbles = bubbleRectLinkedHashMap.keySet();
        for (LayoutHelper.Bubble bubble : bubbles) {
            View child = mViewBubbleMap.get(bubble);
            Rect rect = bubbleRectLinkedHashMap.get(bubble);
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    @VisibleForTesting
    public void showBubbleAnim() {
        startLayoutAnimation();
    }

    @VisibleForTesting
    public void repaint() {
    }

    @VisibleForTesting
    public void doubleBubbles() {
    }
}

