package yhb.dc.demo.customview.custom_view.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LayoutHelper {

    private int mHeight, mWidth;
    private int maxBubbleRadius = Integer.MIN_VALUE, minBubbleRadius = Integer.MAX_VALUE;

    private final BubbleProvider mBubbleProvider;
    private final List<Bubble> mBufferJar = new ArrayList<>();

    private int x = 0, y = 0;
    private int scanStep = 3;

    private LinkedHashMap<Bubble, Rect> result = new LinkedHashMap<>();

    public LayoutHelper(int height, BubbleProvider bubbleProvider) {
        mHeight = height;
        mBubbleProvider = bubbleProvider;
        int[] bubbleRadius = bubbleProvider.getAllRadius();
        for (int radius : bubbleRadius) {
            if (maxBubbleRadius < radius) maxBubbleRadius = radius;
            if (minBubbleRadius > radius) minBubbleRadius = radius;
        }

    }

    public LinkedHashMap<Bubble, Rect> doJob() {
        mBufferJar.clear();
        x = maxBubbleRadius / 2;
        y = maxBubbleRadius / 2;
        int childCount = mBubbleProvider.getCount();
        int borderRight = 0;
        for (int i = 0; i < childCount; i++) {
            Pair<Bubble, Rect> bubbleRectPair = compute();
            if (bubbleRectPair != null) {
                layout(bubbleRectPair.first, bubbleRectPair.second);
                if (bubbleRectPair.second.right > borderRight) {
                    borderRight = bubbleRectPair.second.right;
                }
                mBufferJar.add(bubbleRectPair.first);
            }
        }
        int offset = borderRight - mWidth;
        if (offset != 0) {
            mWidth += offset;
        }

        return result;
    }

    private Pair<Bubble, Rect> compute() {
        Bubble bubble = mBubbleProvider.nextBubble();

        if (bubble == null) {
            return null;
        }

        boolean overlap;

        while ((overlap = checkOverlap(bubble, x, y))) {
            Bubble rePick = mBubbleProvider.askSmaller(bubble);
            if (bubble == rePick) {
                break;
            }
        }

        int radius = mBubbleProvider.getRadius(bubble);

        if (overlap) {
            availableCenter(bubble);
        }

        Rect rect = new Rect(x - radius, y - radius, x + radius, y + radius);

        return new Pair<>(bubble, rect);
    }

    private void layout(Bubble bubble, Rect rect) {
        result.put(bubble, rect);
    }

    private void availableCenter(Bubble bubble) {
        while (true) {
            if (y >= mHeight - maxBubbleRadius / 2) {
                x += scanStep;
                y = maxBubbleRadius / 2;
            }
            if (checkOverlap(bubble, x, y)) {
                y += scanStep;
            } else {
                return;
            }
        }
    }

    private boolean checkOverlap(Bubble bubble, int x, int y) {
        int radius = mBubbleProvider.getRadius(bubble);
        Rect rect = new Rect(x - radius, y - radius, x + radius, y + radius);
        if (mBufferJar.isEmpty()) return false;
        int maxChildCountVertical = mHeight / minBubbleRadius;
        for (int i = mBufferJar.size() - 1; i >= Math.max(mBufferJar.size() - maxChildCountVertical * 2 - 1, 0); i--) {
            Bubble other = mBufferJar.get(i);
            if (checkOverlap(rect, result.get(other))) return true;
        }
        return false;
    }

    private boolean checkOverlap(Rect circle0, Rect circle1) {
        if (circle1 == null || circle0 == null) {
            return false;
        }

        float dx = circle1.centerX() - circle0.centerX();
        float dy = circle1.centerY() - circle0.centerY();
        float r = circle1.height() / 2 + circle0.height() / 2;
        float d = (dx * dx) + (dy * dy);

        return d < r * r;
    }

    public View getView(Bubble bubble, Context context) {
        return mBubbleProvider.getView(bubble, context);
    }


    public interface BubbleProvider {

        int getCount();

        Bubble nextBubble();

        Bubble askSmaller(Bubble bubble);

        void reset();

        View getView(Bubble bubble, Context context);

        int getId(int index);

        int getRadius(Bubble bubble);

        int[] getAllRadius();
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
