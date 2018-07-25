package yhb.dc.demo.demo_view.custom_view.widget;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import yhb.dc.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by yhb on 18-7-21.
 */

public class BubbleLayoutCopy extends RelativeLayout implements View.OnClickListener {

    private static final Spec SPEC = new Spec();
    private static final int ID_BASE = 0x5078;


    private List<List<ImageView>> mBubbleRows;

    public BubbleLayoutCopy(@NonNull Context context) {
        this(context, null);
    }

    public BubbleLayoutCopy(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayoutCopy(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_VERTICAL);
        mBubbleRows = new ArrayList<>();

        for (int i = 0; i < SPEC.rowCount; i++) {
            mBubbleRows.add(new ArrayList<ImageView>());
        }

        createRandomCircles();

    }

    public void createRandomCircles() {

        for (int i = 0; i < SPEC.rowCount; i++) {
            mBubbleRows.get(i).clear();
        }
        removeAllViews();
        SPEC.reset();


        int itemCount = 0;
        for (int i = 0; i < SPEC.rowCount; i++) {
            for (int j = 0; j < SPEC.columnCount; j++) {
                LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                if (j != 0) {/* left first child, with no left anchor */
                    layoutParams.addRule(END_OF, mBubbleRows.get(i).get(j - 1).getId());
                }
                if (i != 0) {/* top first row's children, with no top anchor */
                    layoutParams.addRule(BELOW, mBubbleRows.get(i - 1).get(j).getId());
                }
                int id = ID_BASE + ++itemCount;

                Bubble bubble = SPEC.nextBubble();

                if (bubble == null) {
                    return;
                }

                CircleImageView imageView = new CircleImageView(getContext());
                imageView.setOnClickListener(this);
                imageView.setId(id);
                imageView.setImageResource(bubble.selected ? bubble.selectedRes : bubble.unselectedRes);
                imageView.setBorderColor(Color.TRANSPARENT);
                imageView.setBorderOverlay(false);
                imageView.setBorderWidth(3);
                imageView.setText(bubble.label);
                addView(imageView, layoutParams);
                mBubbleRows.get(i).add(imageView);
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(142 * 6, MeasureSpec.EXACTLY));
    }

    @Override
    public void onClick(View v) {
        clearOverlap(v);
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

    public void randomMarginTop() {
        List<ImageView> imageViews = mBubbleRows.get(0);
        for (ImageView child : imageViews) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            layoutParams.topMargin = (int) (Math.random() * 50);
            child.setLayoutParams(layoutParams);
        }
    }

    public boolean clearOverlap() {
        boolean adjusted = false;
        for (int j = 0; j < SPEC.columnCount; j++) {
            for (int i = 0; i < SPEC.rowCount; i++) {
                if (!adjusted) adjusted = clearOverlap(mBubbleRows.get(i).get(j));
                else clearOverlap(mBubbleRows.get(i).get(j));
            }
        }
        return adjusted;
    }

    private void clearOverlap(int i, int j) {

        if (i < 0 || i > mBubbleRows.size() - 1 || j < 0 || j >= mBubbleRows.get(0).size() - 1)
            return;

        View child = mBubbleRows.get(i).get(j);
        for (int k = 0; k < SPEC.rowCount; k++) {
            View right = mBubbleRows.get(k).get(j + 1);
            if (right != child && right != null) {
                Vector2D offset = getOffsetVector(child, right);
                LayoutParams layoutParams = (LayoutParams) right.getLayoutParams();
                // 重叠了 offset 像素点，我们把下面的 view 右下角移动 offset 像素
                layoutParams.leftMargin += offset.componentX();
                layoutParams.topMargin += offset.componentY();
                right.setLayoutParams(layoutParams);
            }
        }

    }

    public boolean clearOverlap(View child) {
        boolean adjusted = false;
        int childCount = getChildCount();
        for (int j = 0; j < childCount; j++) {
            View other = getChildAt(j);
            if (other != child && other != null) {
                Vector2D offset = getOffsetVector(child, other);
                LayoutParams layoutParams = (LayoutParams) other.getLayoutParams();
                // 重叠了 offset 像素点，我们把下面的 view 右下角移动 offset 像素
                layoutParams.leftMargin += Math.max(0, offset.componentX());
                layoutParams.topMargin += Math.max(0, offset.componentY());

                if (Math.max(0, offset.componentX()) > 1f || Math.max(0, offset.componentY()) > 1f) {
                    adjusted = true;
                }
                other.setLayoutParams(layoutParams);
            }
        }
        return adjusted;
    }

    private Vector2D getOffsetVector(View child, View bottomChild) {
        if (bottomChild == null || child == null) {
            return new Vector2D(0, 0, 0, 0);
        }
        Rect rect1 = getRect(child);
        Rect rect2 = getRect(bottomChild);
        Circle c1 = new Circle(rect1.centerX(), rect1.centerY(), rect1.height() / 2);
        Circle c2 = new Circle(rect2.centerX(), rect2.centerY(), rect2.height() / 2);
        if (c1 != c2) {
            float dx = c2.x - c1.x;
            float dy = c2.y - c1.y;
            float r = c1.radius + c2.radius;
            float d = (dx * dx) + (dy * dy);
            if (d < r * r) {
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

    public void doubleBubbles() {
        SPEC.doubleBubbles();
        createRandomCircles();
        invalidate();
    }


    static class Bubble {
        boolean selected;
        int level;
        String label;
        int selectedRes;
        int unselectedRes;

        Bubble(boolean selected, int level, String label, int selectedRes, int unselectedRes) {
            this.selected = selected;
            this.level = level;
            this.label = label;
            this.selectedRes = selectedRes;
            this.unselectedRes = unselectedRes;
        }

    }

    static class Spec {

        final List<Bubble> mBubbles = new LinkedList<>();
        final List<Bubble> mBufferJar = new LinkedList<>();

        int rowCount = 3;
        int columnCount = 0;

        private int mRepeatCount = 1;

        Spec() {
            initBubble();
        }

        void doubleBubbles() {
            mRepeatCount++;
            reset();
        }

        private void initBubble() {

            for (int i = 0; i < mRepeatCount; i++) {
                mBubbles.add(new Bubble(false, 1, "科幻", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(true, 1, "动作", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "喜剧", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "恐怖", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(true, 1, "动漫", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "美剧", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(true, 1, "韩剧", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "偶像剧", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "宫斗剧", R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));

                mBubbles.add(new Bubble(false, 2, "战争", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "犯罪", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "爱情", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "惊悚", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(true, 2, "悬疑", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "文艺", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "老电影", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "日剧", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "TVB", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "泰剧", R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));

                mBubbles.add(new Bubble(false, 3, "冒险", R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 3, "青春", R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 3, "小众片", R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(true, 3, "英剧", R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 3, "破案剧", R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 3, "穿越剧", R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));

                mBubbles.add(new Bubble(false, 4, "奇幻", R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));
                mBubbles.add(new Bubble(false, 4, "剧情", R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));
                mBubbles.add(new Bubble(true, 4, "伦理", R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));
                mBubbles.add(new Bubble(false, 4, "武侠", R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));
                mBubbles.add(new Bubble(false, 4, "谍战剧", R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));

            }

            columnCount = mBubbles.size() / BubbleLayoutCopy.SPEC.rowCount;

        }

        Bubble nextBubble() {
            if (mBubbles.isEmpty()) {
                return null;
            }
            int index = (int) (Math.random() * mBubbles.size());
            Bubble bubble = mBubbles.remove(index);
            mBufferJar.add(bubble);
            return bubble;
        }


        void reset() {
            mBubbles.clear();
            mBufferJar.clear();
            initBubble();
        }
    }

    static class Circle {
        public float x, y, radius;

        Circle(float x, float y, float radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }


        float d2Point(PointF center) {
            float dx = x - center.x;
            float dy = y - center.y;
            return (int) Math.sqrt(dx * dx + dy * dy);
        }
    }

}
