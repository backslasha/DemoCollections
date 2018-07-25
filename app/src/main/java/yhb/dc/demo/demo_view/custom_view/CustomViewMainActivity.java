package yhb.dc.demo.demo_view.custom_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.demo.demo_view.custom_view.widget.BubbleLayout;
import yhb.dc.demo.demo_view.custom_view.widget.BubbleLayoutCopy;

/**
 * Created by yhb on 18-3-15.
 */

public class CustomViewMainActivity extends AppCompatActivity implements Demo {

    private BubbleLayout mBubbleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        mBubbleLayout = findViewById(R.id.bubble_layout);
    }

    public void animate(final View view) {
//        mBubbleLayout.showBubbleAnim();
    }

    public void randomMarginTop(View view) {

    }

    public void clearOverlap(final View view) {

    }

    public void repaint(View view) {
        mBubbleLayout.repaint();
    }

    public void doubleBubble(View view) {
//        mBubbleLayout.doubleBubbles();
    }
}
