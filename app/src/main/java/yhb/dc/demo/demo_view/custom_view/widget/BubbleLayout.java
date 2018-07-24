package yhb.dc.demo.demo_view.custom_view.widget;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.graphics.Color.*;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by yhb on 18-7-21.
 */

public class BubbleLayout extends RelativeLayout implements View.OnClickListener {

    private ArrayList<ImageView> mBubbleRow1, mBubbleRow2, mBubbleRow3;

    public BubbleLayout(@NonNull Context context) {
        this(context, null);
    }

    public BubbleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setBackgroundColor(Color.WHITE);
        mBubbleRow1 = new ArrayList<>();
        mBubbleRow2 = new ArrayList<>();
        mBubbleRow3 = new ArrayList<>();

        int base = 0x5078;
        for (int i = 0; i < 30; i++) {
            int id = base++;
            CircleImageView imageView = new CircleImageView(getContext());
            imageView.setOnClickListener(this);
            imageView.setId(id);
            imageView.setImageDrawable(new ColorDrawable(Spec.getColor()));
            addView(imageView);

            if (mBubbleRow1.size() < 10) {
                mBubbleRow1.add(imageView);
            } else if (mBubbleRow2.size() < 10) {
                mBubbleRow2.add(imageView);
            } else if (mBubbleRow3.size() < 10) {
                mBubbleRow3.add(imageView);
            } else {
                throw new IllegalStateException();
            }
        }


        int size = mBubbleRow1.size();
        ImageView leftPtr = null;
        ImageView upPtr = null;
        for (int i = 0; i < size; i++) {
            ImageView imageView = mBubbleRow1.get(i);
            int widgetSize = Spec.getSize();
            LayoutParams layoutParams = new LayoutParams(widgetSize, widgetSize);

            if (leftPtr != null) {
                layoutParams.addRule(END_OF, leftPtr.getId());
            }

            leftPtr = imageView;
        }

        upPtr = mBubbleRow1.get(0);
        leftPtr = null;
        for (int i = 0; i < size; i++) {
            ImageView imageView = mBubbleRow2.get(i);
            int widgetSize = Spec.getSize();
            LayoutParams layoutParams = new LayoutParams(widgetSize, widgetSize);

            if (leftPtr != null) {
                layoutParams.addRule(END_OF, leftPtr.getId());
            }

            if (upPtr != null) {
                layoutParams.addRule(ABOVE, upPtr.getId());
            }

            leftPtr = imageView;
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int width = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            width += child.getMeasuredWidth();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), v.getId() + "", Toast.LENGTH_SHORT).show();
    }

    public void bubble() {

        for (int i = 10; i < 20; i++) {
            final View viewById = getChildAt(i);
            ValueAnimator animator = new ValueAnimator();
            double upper = Math.random() * 50;
            double lower = Math.random() * -50;
            double duration = (long) (1000 + Math.random() * 1000);
            animator.setIntValues((int) lower, (int) upper);
            animator.setDuration((long) duration);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setRepeatCount(-1);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setEvaluator(new IntEvaluator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewById.getLayoutParams();
                    if (layoutParams != null) {
                        int animatedValue = (int) animation.getAnimatedValue();
                        layoutParams.bottomMargin = animatedValue;
                        layoutParams.leftMargin = animatedValue;
                        layoutParams.rightMargin = animatedValue;
                        layoutParams.topMargin = animatedValue;
                        viewById.setLayoutParams(layoutParams);
                    }
                }
            });
            animator.start();
        }
    }

    public void random() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            layoutParams.bottomMargin = (int) (Math.random() * 10 + 3);
            layoutParams.leftMargin = (int) (Math.random() * 10 + 3);
            layoutParams.rightMargin = (int) (Math.random() * 10 + 3);
            layoutParams.topMargin = (int) (Math.random() * 10 + 3);

            if (i == 0 || i == 20) {
                layoutParams.leftMargin = (int) (Math.random() * 200 + 100);
            }

            if (i != 0) {
                View upLeft = getChildAt(i - 1);
            }

            child.setLayoutParams(layoutParams);
        }
    }


    class Circle {
        int x, y, r;
    }

    static class Spec {
        private static final int SIZE_1 = 100 * 2;
        private static final int SIZE_2 = 115 * 2;
        private static final int SIZE_3 = 128 * 2;
        private static final int SIZE_4 = 142 * 2;
        private static final int[] SPEC = {SIZE_1, SIZE_2, SIZE_3, SIZE_4};
        private static final int[] COLOR = {BLUE, RED, GREEN, YELLOW, CYAN, BLACK,};


        private static int getSize() {
            return SPEC[(int) (Math.random() * 4)];
        }

        static int index = 0;

        private static @ColorInt
        int getColor() {
            return COLOR[(++index) % COLOR.length];
        }
    }
}
