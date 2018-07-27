package yhb.dc.demo.demo_view.custom_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.demo.demo_view.custom_view.widget.BubbleLayout;
import yhb.dc.demo.demo_view.custom_view.widget.BubbleLayout1;
import yhb.dc.demo.demo_view.custom_view.widget.BubbleLayoutCopy;

/**
 * Created by yhb on 18-3-15.
 */

public class CustomViewMainActivity extends AppCompatActivity implements Demo {

    private BubbleLayout mBubbleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_view);
        mBubbleLayout = findViewById(R.id.bubble_layout);
    }

    public void animate(final View view) {
        mBubbleLayout.showBubbleAnim();
    }

    public void repaint(View view) {
        mBubbleLayout.repaint();
    }

    public void doubleBubble(View view) {
        mBubbleLayout.doubleBubbles();
    }

    public void increaseHeight(View view) {
        ViewGroup.LayoutParams layoutParams = mBubbleLayout.getLayoutParams();
        layoutParams.height += 20;
        mBubbleLayout.setLayoutParams(layoutParams);
    }

    public void decreaseHeight(View view) {
        ViewGroup.LayoutParams layoutParams = mBubbleLayout.getLayoutParams();
        layoutParams.height -= 20;
        mBubbleLayout.setLayoutParams(layoutParams);
    }
}
