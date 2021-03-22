package yhb.dc.demo.customview.custom_view.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.VisibleForTesting;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import yhb.dc.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static yhb.dc.demo.customview.custom_view.widget.BubbleLayout.*;

public class BubbleProviderImpl implements BubbleProvider {
    private final static int ID_PREFIX = 0x5078;

    final List<Bubble> mBubbles = new LinkedList<>();
    final List<Bubble> mBufferJar = new LinkedList<>();
    final List<BubbleGroup> mBubbleGroups = new ArrayList<>();

    private int rowCount = 3;
    private int columnCount = 0;
    private int mRepeatCount = 1;

    BubbleProviderImpl() {
        initBubble();
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


        int minLevel = Integer.MAX_VALUE, maxLevel = Integer.MIN_VALUE;
        final SparseArray<List<Bubble>> levelGroup = new SparseArray<>();
        for (Bubble bubble : mBubbles) {
            List<Bubble> bubbles = levelGroup.get(bubble.level);
            if (bubbles == null) {
                bubbles = new ArrayList<>();
                minLevel = minLevel > bubble.level ? bubble.level : minLevel;
                maxLevel = maxLevel < bubble.level ? bubble.level : maxLevel;
                levelGroup.put(bubble.level, bubbles);
            }
            bubbles.add(bubble);
        }

        for (int i = minLevel; i <= maxLevel; i++) {
            List<Bubble> bubbles = levelGroup.get(i);
            if (bubbles != null) {
                mBubbleGroups.add(new BubbleGroup(i, bubbles, mBubbles.size()));
            }
        }

    }

    @Override
    public Bubble nextBubble() {
        if (mBubbles.size() == mBufferJar.size()) {
            return null;
        }
        float maxIncreaseSpeed = Integer.MIN_VALUE;
        int select = -1;
        for (int i = mBubbleGroups.size() - 1; i >= 0; i--) {
            if (mBubbleGroups.get(i).getRatioIncreaseSpeed() > maxIncreaseSpeed) {
                select = i;
                maxIncreaseSpeed = mBubbleGroups.get(i).getRatioIncreaseSpeed();
            }
        }
        Bubble bubble = mBubbleGroups.get(select).bubbles.remove(0);
        mBufferJar.add(bubble);

        for (BubbleGroup bubbleGroup : mBubbleGroups) {
            bubbleGroup.updateRatio(mBubbles.size() - mBufferJar.size());
        }
        return bubble;
    }

    @Override
    public Bubble askSmaller(Bubble bubble) {
        return null;
    }


    @Override
    public void reset() {
        mBubbles.clear();
        mBufferJar.clear();
        initBubble();
    }

    @Override
    public int getCount() {
        return columnCount * rowCount;
    }

    @Override
    public View getView(Bubble bubble, Context context) {
        CircleImageView imageView = new CircleImageView(context);
        imageView.setImageResource(bubble.selected ? bubble.selectedRes : bubble.unselectedRes);
        imageView.setBorderColor(Color.WHITE);
        imageView.setTextSize(16);
        imageView.setTextColor(bubble.selected ? bubble.selectedTextColor : bubble.unselectedTextColor);
        imageView.setBorderOverlay(false);
        imageView.setBorderWidth(0);
        imageView.setText(bubble.label);
        imageView.setLayoutParams(new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        return imageView;
    }

    @Override
    public int getId(int index) {
        return ID_PREFIX + index;
    }

    @VisibleForTesting
    public void doubleBubbles() {
        mRepeatCount++;
        reset();
    }



}
