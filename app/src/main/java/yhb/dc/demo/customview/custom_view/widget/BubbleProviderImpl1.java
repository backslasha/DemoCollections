package yhb.dc.demo.customview.custom_view.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.VisibleForTesting;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import yhb.dc.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static yhb.dc.demo.customview.custom_view.widget.LayoutHelper.*;

public class BubbleProviderImpl1 implements BubbleProvider {
    private final static int ID_PREFIX = 0x5078;

    private Context mContext;
    final List<Bubble> mBubbles = new LinkedList<>();
    final List<Bubble> mBufferJar = new LinkedList<>();
    final List<BubbleGroup> mBubbleGroups = new ArrayList<>();
    final SparseArray<BubbleGroup> mBubbleGroupMap = new SparseArray<>();

    private int rowCount = 3;
    private int columnCount = 0;
    private int mRepeatCount = 1;

    BubbleProviderImpl1(Context context) {
        mContext = context;
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
                BubbleGroup bubbleGroup = new BubbleGroup(i, bubbles, mBubbles.size());
                mBubbleGroups.add(bubbleGroup);
                mBubbleGroupMap.put(bubbleGroup.level, bubbleGroup);
            }
        }

    }

    @Override
    public Bubble nextBubble() {
        if (mBubbles.size() == mBufferJar.size() || mBubbleGroups.isEmpty()) {
            return null;
        }
        int select = -1;
        for (int i = mBubbleGroups.size() - 1; i >= 0; i--) {
            if (mBubbleGroups.get(i).bubbles.size() > 0) {
                select = i;
                break;
            }
        }

        if (select == -1) {
            return null;
        }

        for (int i = mBubbleGroups.size() - 1; i >= 0; i--) {
            if (mBubbleGroups.get(i).bubbles.size() > 0) {
                if (mBubbleGroups.get(i).bubbles.size() > mBubbleGroups.get(select).bubbles.size()) {
                    select = i;
                }
            }
        }
        Bubble bubble = mBubbleGroups.get(select).bubbles.remove(0);
        mBufferJar.add(bubble);
        return bubble;
    }


    @Override
    public Bubble askSmaller(Bubble bubble) {
        if (bubble == null) {
            return null;
        }
        Bubble rePick = bubble;
        BubbleGroup theGroup = mBubbleGroupMap.get(bubble.level);

        for (int i = bubble.level - 1; i >= 1; i--) {
            BubbleGroup bubbleGroup = mBubbleGroupMap.get(i);
            if (bubbleGroup != null && !bubbleGroup.bubbles.isEmpty()) {
                rePick = bubbleGroup.bubbles.remove(0);
                theGroup.bubbles.add(bubble);
                mBufferJar.remove(bubble);
                mBufferJar.add(rePick);
                break;
            }
        }

        return rePick;
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

    @Override
    public int getRadius(Bubble bubble) {
        switch (bubble.level) {
            case 1:
                return dp(70/2, mContext);
            case 2:
                return dp(80/2, mContext);
            case 3:
                return dp(100/2, mContext);
            case 4:
                return dp(120/2, mContext);
        }
        return 0;
    }

    @Override
    public int[] getAllRadius() {
        return new int[]{
                dp(70, mContext),
                dp(80, mContext),
                dp(100, mContext),
                dp(120, mContext)
        };
    }

    @VisibleForTesting
    public void doubleBubbles() {
        mRepeatCount++;
        reset();
    }

    private int dp(int x, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                x, context.getResources().getDisplayMetrics());
    }
}

