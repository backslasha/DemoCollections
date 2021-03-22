package yhb.dc.demo.customview.custom_view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import yhb.dc.R;

import yhb.dc.common.Demo;
import yhb.dc.demo.customview.custom_view.widget.BubbleLayout;

/**
 * Created by yhb on 18-3-15.
 */
@Demo(id = Demo.DEMO_ID_BUBBLE_LAYOUT)
public class CustomViewMainActivity extends AppCompatActivity {

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
