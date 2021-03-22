package yhb.dc.demo.customview.custom_view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import yhb.dc.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by yhb on 18-7-21.
 */

public class BubbleLayoutCopy1 extends RelativeLayout implements View.OnClickListener {

    private static final Spec SPEC = new Spec();
    private static final int ID_BASE = 0x5078;


    private List<List<ImageView>> mBubbleRows;

    public BubbleLayoutCopy1(@NonNull Context context) {
        this(context, null);
    }

    public BubbleLayoutCopy1(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayoutCopy1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_VERTICAL);

        mBubbleRows = new ArrayList<>();

        for (int i = 0; i < SPEC.rowCount; i++) {
            mBubbleRows.add(new ArrayList<ImageView>());
        }

        createRandomCircles();

        setupAnimation();

        // 布局结束之后清除重叠
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                randomMarginTop(50);
                clearAllOverlap();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(142 * 6, MeasureSpec.EXACTLY));
    }

    @Override
    public void onClick(View v) {
        clearOverlap(v);
    }

    private void setupAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.bubble_show);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
        controller.setInterpolator(new OvershootInterpolator());
        controller.setDelay(0.1f);
        setLayoutAnimation(controller);
    }

    public void showBubbleAnim() {
        hideAllViews();
        showAllViews();
    }

    /*------------ clear overlap ----------------*/
    private boolean isOverlap = true;
    private int clearOverlapCount = 0;

    /**
     * 反复调用 clearOverlap()（由于每次将重叠的 ImageView 往右下角挤，因此这个过程很快会结束，30 个 ImageView 时一般会调用 3~5 次），直至所有 ImageView 都不重叠
     */
    private void clearAllOverlap() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isOverlap) {
                    clearOverlapCount++;
                    isOverlap = clearOverlap();
                } else {
                    showAllViews();
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Toast.makeText(getContext(), "clear overlap count:" + clearOverlapCount + ".", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (isOverlap) {
            hideAllViews();
            isOverlap = clearOverlap();
            clearOverlapCount++;
        }
    }

    /**
     * 对所有 ImageView 进行一次的去重叠（将每个重叠的 ImageView 往右下角挤），注意，该过程完不能保证所有 ImageView 之间互相不重叠
     * @return 本次调用是否用做出重叠调整，若无，说明所有 ImageView 之间都互不重叠
     */
    private boolean clearOverlap() {
        boolean adjusted = false;
        for (int j = 0; j < SPEC.columnCount; j++) {
            for (int i = 0; i < SPEC.rowCount; i++) {
                if (!adjusted) adjusted = clearOverlap(mBubbleRows.get(i).get(j));
                else clearOverlap(mBubbleRows.get(i).get(j));
            }
        }
        return adjusted;
    }

    /**
     * 清除单个 ImageView 的重叠（将每个重叠的 ImageView 往右下角挤）
     * @return 本次调用是否用做出重叠调整，若无，说明该 ImageView 不和任何其他 ImageView 重叠
     */
    private boolean clearOverlap(View child) {
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

    /**
     * 给第一行的 ImageView 随机添加 marginTop 达到轻微打乱布局的目的
     */
    private void randomMarginTop(int max) {
        List<ImageView> imageViews = mBubbleRows.get(0);
        for (ImageView child : imageViews) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            layoutParams.topMargin = (int) (Math.random() * max);
            child.setLayoutParams(layoutParams);
        }
        isOverlap = true;
        clearOverlapCount = 0;
    }

    /*--------------- repaint -----------------------*/

    public void doubleBubbles() {
        SPEC.doubleBubbles();
        invalidate();
    }

    /**
     * 根据 SPEC 重新生成及添加若干个 ImageView 并添加相应的布局规则
     */
    private void createRandomCircles() {

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
                imageView.setBorderWidth(0);
                imageView.setText(bubble.label);
                addView(imageView, layoutParams);
                mBubbleRows.get(i).add(imageView);
            }
        }
    }

    private void hideAllViews() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setVisibility(INVISIBLE);
        }
    }

    private void showAllViews() {
        startLayoutAnimation();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setVisibility(VISIBLE);
        }
    }

    @Override
    public void invalidate() {
        // 布局结束之后清除重叠
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                randomMarginTop(50);
                clearAllOverlap();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        createRandomCircles();
        super.invalidate();
    }

    /*------------------ 辅助计算 -------------------------------*/

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

    private Rect getRect(View child) {
        return new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
    }

    private static class Bubble {
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

    private static class Spec {

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
