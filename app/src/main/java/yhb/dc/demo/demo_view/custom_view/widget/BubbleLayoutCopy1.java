package yhb.dc.demo.demo_view.custom_view.widget;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.CYAN;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by yhb on 18-7-21.
 */

public class BubbleLayoutCopy1 extends RelativeLayout implements View.OnClickListener {

    private static final int MIN_PADDING = 0;
    private final ArrayList<View> mChildren;
    private ArrayList<ImageView> mBubbleRow1, mBubbleRow2, mBubbleRow3;

    public BubbleLayoutCopy1(@NonNull Context context) {
        this(context, null);
    }

    public BubbleLayoutCopy1(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayoutCopy1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setBackgroundColor(Color.WHITE);
        setGravity(Gravity.CENTER_VERTICAL);
        mBubbleRow1 = new ArrayList<>();
        mBubbleRow2 = new ArrayList<>();
        mBubbleRow3 = new ArrayList<>();

        int num = 0;
        int base = 0x5078;
        int size;
        for (int j = 0; j < 10; j++) {
            size = Spec.getSize();
            int id = base++;
            LayoutParams layoutParams = new LayoutParams(size, size);
            if (j != 0) {
                layoutParams.addRule(END_OF, id - 1);
            }

            CircleImageView imageView = new CircleImageView(getContext());
            imageView.setOnClickListener(this);
            imageView.setId(id);
            imageView.setImageResource(Spec.getSRC());
            imageView.setText(String.valueOf(++num));
            addView(imageView, layoutParams);
        }

        for (int j = 0; j < 10; j++) {
            int id = base++;
            size = Spec.getSize();
            LayoutParams layoutParams = new LayoutParams(size, size);
            if (j != 0) {
                layoutParams.addRule(END_OF, id - 1);
            }
            layoutParams.addRule(BELOW, id - 10);
            CircleImageView imageView = new CircleImageView(getContext());
            imageView.setId(id);
            imageView.setImageResource(Spec.getSRC());
            imageView.setOnClickListener(this);
            imageView.setText(String.valueOf(++num));
            addView(imageView, layoutParams);
        }
        for (int j = 0; j < 10; j++) {
            int id = base++;
            size = Spec.getSize();
            LayoutParams layoutParams = new LayoutParams(size, size);
            if (j != 0) {
                layoutParams.addRule(END_OF, id - 1);
            }
            layoutParams.addRule(BELOW, id - 10);
            CircleImageView imageView = new CircleImageView(getContext());
            imageView.setId(id);
            imageView.setOnClickListener(this);
            imageView.setImageResource(Spec.getSRC());
            imageView.setText(String.valueOf(++num));
            addView(imageView, layoutParams);
        }

        mChildren = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            mChildren.add(getChildAt(i));
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
        clearOverlap();
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
                    MarginLayoutParams layoutParams = (MarginLayoutParams) viewById.getLayoutParams();
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
        for (int i = 0; i < 10; i++) {
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
//            layoutParams.bottomMargin = (int) (Math.randomMarginTop() * 20);
            layoutParams.leftMargin = (int) (/*Math.randomMarginTop() **/ 0);
            layoutParams.rightMargin = (int) (/*Math.randomMarginTop() **/ 0);
            layoutParams.topMargin = (int) (Math.random() * 100);

            if (i == 0 || i == 20) {
                layoutParams.leftMargin = (int) (Math.random() * 200 + 100);
            }

            child.setLayoutParams(layoutParams);
        }
    }

    public void clearOverlap() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            for (int j = i + 1; j < childCount; j++) {
                View child = getChildAt(i);
                View other = getChildAt(j);
                if (other != null) {
                    Vector2D offset = checkOverlap(child, other);
                    LayoutParams layoutParams = (LayoutParams) other.getLayoutParams();
                    // 重叠了 offset 像素点，我们把下面的 view 下移 offset-MIN_PADDING 像素
                    layoutParams.leftMargin += offset.componentX();
                    layoutParams.topMargin += offset.componentY();
                    other.setLayoutParams(layoutParams);
                }
            }
        }
    }

    public void clearOverlap(View child) {
        int childCount = getChildCount();
        for (int j = 0; j < childCount; j++) {
            View other = getChildAt(j);
            if (other != child && other != null) {
                Vector2D offset = checkOverlap(child, other);
                LayoutParams layoutParams = (LayoutParams) other.getLayoutParams();
                // 重叠了 offset 像素点，我们把下面的 view 下移 offset-MIN_PADDING 像素
                layoutParams.leftMargin += offset.componentX();
                layoutParams.topMargin += offset.componentY();
                other.setLayoutParams(layoutParams);
            }
        }
    }

    private Vector2D checkOverlap(View child, View bottomChild) {

        if (bottomChild == null || child == null) {
            return new Vector2D(0, 0, 0, 0);
        }
        Rect rect1 = getRect(child);
        Rect rect2 = getRect(bottomChild);
        Circle c1 = new Circle(rect1.centerX(), rect1.centerY(), rect1.height() / 2, Spec.getColor());
        Circle c2 = new Circle(rect2.centerX(), rect2.centerY(), rect2.height() / 2, Spec.getColor());
        if (c1 != c2) {
            float dx = c2.x - c1.x;
            float dy = c2.y - c1.y;
            float r = c1.radius + c2.radius;
            float d = (dx * dx) + (dy * dy);
            if (d < (r * r) - 0.01) {// - 0.01 why?
                double sqrt_d = Math.sqrt(d);
                float x = (float) (r * dx / sqrt_d - dx);
                float y = (float) (r * dy / sqrt_d - dy);
                return new Vector2D(0, x, 0, y);
            }
        }
        return new Vector2D(0, 0, 0, 0);
    }


    public Rect getRect(View child) {
        return new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
    }


    static class Spec {
        private static final int SIZE_1 = 100 * 2;
        private static final int SIZE_2 = 115 * 2;
        private static final int SIZE_3 = 128 * 2;
        private static final int SIZE_4 = 142 * 2;
        private static final int[] SPEC = {SIZE_1, SIZE_2, SIZE_3, SIZE_4};
        private static final int[] COLOR = {BLUE, RED, GREEN, YELLOW, CYAN, BLACK, BLUE, RED, GREEN, YELLOW,};
        public static final int[] SRC = {
                R.drawable.bubble1_2x,
                R.drawable.bubble2_2x,
                R.drawable.bubble3_2x,
                R.drawable.bubble4_2x,
                R.drawable.bubble1_red_2x,
                R.drawable.bubble2_red_2x,
                R.drawable.bubble3_red_2x,
                R.drawable.bubble4_red_2x
        };

        private static int getSize() {
            return WRAP_CONTENT;
        }

        static int index = 0;

        private static @ColorInt
        int getColor() {
            return COLOR[(++index) % COLOR.length];
        }

        private static @DrawableRes
        int getSRC() {
            return SRC[(int) (Math.random() * SRC.length)];
        }
    }

    static class BubbleLayoutAdapter {

        private List<Circle> allCircles = new ArrayList<>();

        public BubbleLayoutAdapter() {
            allCircles = createRandomCircles(30);
        }

        public void flatCircles(List<Circle> allCircles) {

            List<Circle> sortedCircles = this.allCircles;

            // Cycle through circles for collision detection
            for (int i = 0; i < sortedCircles.size(); i++) {
                for (int j = i; j < sortedCircles.size(); j++) {
                    Circle c1 = sortedCircles.get(i);
                    Circle c2 = sortedCircles.get(j);
                    if (c1 != c2) {
                        float dx = c2.x - c1.x;
                        float dy = c2.y - c1.y;
                        float r = c1.radius + c2.radius;
                        float d = (dx * dx) + (dy * dy);
                        if (d < (r * r) - 0.01) {// - 0.01 why?
                            double sqrt_d = Math.sqrt(d);
                            float x = (float) (r * dx / sqrt_d - dx);
                            float y = (float) (r * dy / sqrt_d - dy);
                            c2.y += y;
                            c2.x += x;
                        }
                    }
                }
            }
        }

        private List<Circle> createRandomCircles(int circleCount) {
            List<Circle> result = new ArrayList<>();
            int size = Spec.getSize();
            for (int i = 0; i < circleCount; i++) {
                Circle circle = new Circle(size, size, size, Spec.getColor());
                result.add(circle);
            }
            return result;
        }


    }

    static class Circle {
        public final int color;
        public float x, y, radius;

        Circle(float x, float y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }

        float d2Point(PointF center) {
            float dx = x - center.x;
            float dy = y - center.y;
            return (int) Math.sqrt(dx * dx + dy * dy);
        }
    }

}
