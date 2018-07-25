package yhb.dc.demo.demo_view.custom_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.demo.demo_view.custom_view.widget.BubbleLayoutCopy;

/**
 * Created by yhb on 18-3-15.
 */

public class CustomViewMainActivity extends AppCompatActivity implements Demo {

    private BubbleLayoutCopy mBubbleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        mBubbleLayout = findViewById(R.id.bubble_layout);
    }

    public void animate(final View view) {
        mBubbleLayout.bubble();
    }

    public void randomMarginTop(View view) {
        mBubbleLayout.randomMarginTop();
        isOverlap = true;
        clearOverlapCount = 0;
    }

    private boolean isOverlap = true;
    private int clearOverlapCount = 0;

    public void clearOverlap(final View view) {
        mBubbleLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isOverlap) {
                    clearOverlapCount++;
                    isOverlap = mBubbleLayout.clearOverlap();
                } else {
                    mBubbleLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Toast.makeText(CustomViewMainActivity.this, "clear overlap count:" + clearOverlapCount + ".", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (isOverlap) {
            isOverlap = mBubbleLayout.clearOverlap();
            clearOverlapCount++;
        }
    }

    public void invalidate(View view) {
        mBubbleLayout.createRandomCircles();
        mBubbleLayout.invalidate();
        isOverlap = true;
        clearOverlapCount = 0;
    }

    public void doubleBubble(View view) {
        mBubbleLayout.doubleBubbles();
        isOverlap = true;
        clearOverlapCount = 0;
    }
}
