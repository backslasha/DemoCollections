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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
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

        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.bubble_show);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
        controller.setInterpolator(new OvershootInterpolator());
        controller.setDelay(0.1f);
        setLayoutAnimation(controller);

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
                imageView.setTextSize(16);
                imageView.setTextColor(bubble.selected ? bubble.selectedTextColor : bubble.unselectedTextColor);
                imageView.setBorderOverlay(false);
                imageView.setBorderWidth(5);
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

    public void showBubbleAnim() {
        hideAllViews();
        showAllViews();
    }

    public void randomMarginTop() {
        List<ImageView> imageViews = mBubbleRows.get(0);
        for (ImageView child : imageViews) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            layoutParams.topMargin = (int) (Math.random() * 50);
            child.setLayoutParams(layoutParams);
        }
    }

    public void hideAllViews() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setVisibility(INVISIBLE);
        }
    }


    public void showAllViews() {
        startLayoutAnimation();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setVisibility(VISIBLE);
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

        float dx = rect2.centerX() - rect1.centerX();
        float dy = rect2.centerY() - rect1.centerY();
        float r = rect2.height() / 2 + rect1.height() / 2;
        float d = (dx * dx) + (dy * dy);

        if (d < r * r) {
            double sqrt_d = Math.sqrt(d);
            float x = (float) (r * dx / sqrt_d - dx);
            float y = (float) (r * dy / sqrt_d - dy);
            return new Vector2D(0, x, 0, y);
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
                mBubbles.add(new Bubble(false, 1, "科幻", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(true, 1, "动作", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "喜剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "恐怖", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(true, 1, "动漫", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "美剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(true, 1, "韩剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "偶像剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 1, "宫斗剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble1_red_2x, R.drawable.bubble1_2x));
                mBubbles.add(new Bubble(false, 2, "战争", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "犯罪", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "爱情", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "惊悚", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(true, 2, "悬疑", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "文艺", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "老电影", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "日剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "TVB", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 2, "泰剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble2_red_2x, R.drawable.bubble2_2x));
                mBubbles.add(new Bubble(false, 3, "冒险", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 3, "青春", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 3, "小众片", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(true, 3, "英剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 3, "破案剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 3, "穿越剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble3_red_2x, R.drawable.bubble3_2x));
                mBubbles.add(new Bubble(false, 4, "奇幻", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));
                mBubbles.add(new Bubble(false, 4, "剧情", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));
                mBubbles.add(new Bubble(true, 4, "伦理", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));
                mBubbles.add(new Bubble(false, 4, "武侠", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));
                mBubbles.add(new Bubble(false, 4, "谍战剧", 16, Color.WHITE, Color.parseColor("#FF2B50"), R.drawable.bubble4_red_2x, R.drawable.bubble4_2x));

            }

            columnCount = mBubbles.size() / rowCount;

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

}
