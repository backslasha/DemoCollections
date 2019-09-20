package yhb.dc.demo.anim;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import yhb.dc.R;
import yhb.dc.common.DemoBaseActivity;

public class AnimationActivity extends DemoBaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        final Button button = findViewById(R.id.button);

        final float density = getResources().getDisplayMetrics().density;
        final float screenWidth = getResources().getDisplayMetrics().widthPixels / density;
        final float screenHeight = getResources().getDisplayMetrics().heightPixels / density;


        button.post(new Runnable() {
            @Override
            public void run() {
                float dpiCount = button.getWidth() / density;
                Log.d(getTAG(), "Screen Width=" + screenWidth + "dpi");
                Log.d(getTAG(), "Screen Height" + screenHeight + "dpi");
                Log.d(getTAG(), "density=" + density);
                Log.d(getTAG(), "getResources().getDisplayMetrics().densityDpi=" + getResources().getDisplayMetrics().densityDpi);
                Log.d(getTAG(), "button.getWidth() / density=" + dpiCount);
            }
        });


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_translation);
        animation.setDuration(3000);
        button.setAnimation(animation);
        animation.start();

        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setText("卧槽\n\n\n\n\n");
            }
        }, 1500);
//
//        float[] anim = {
//                1f, 1f, 0.7f, 0.7f, 1f, 1f, 1f, 1f, 0.7f, 0.7f, 1f, 1f, 1f, 1f, 0.7f, 0.7f, 1f, 1f, 1f, 1f, 1f, 1f
//        };
//        float[] anim1 = {
//                1f, 1f, 0.7f, 0.7f, 1f, 1f, 1f, 1f, 0.7f, 0.7f, 1f, 1f, 1f, 1f, 0.7f, 0.7f, 1f, 1f, 1f, 1f, 1f, 1f
//        };
//
//        final ObjectAnimator scaleX = ObjectAnimator
//                .ofFloat(button, "scaleX", anim);
//        scaleX.setDuration(1050 * 3 + 400); // 近似实现 -> 每播放缩放动画3次，停 0.4s
//        scaleX.setRepeatMode(ValueAnimator.RESTART);
//        scaleX.setRepeatCount(ValueAnimator.INFINITE);
//        scaleX.setInterpolator(new LinearInterpolator());
//
//        ObjectAnimator scaleY = ObjectAnimator
//                .ofFloat(button, "scaleY", anim);
//        scaleY.setDuration(1050 * 3 + 400);
//        scaleY.setRepeatMode(ValueAnimator.RESTART);
//        scaleY.setRepeatCount(ValueAnimator.INFINITE);
//        scaleY.setInterpolator(new LinearInterpolator());
//
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(scaleX, scaleY);
//        set.start();

    }
}
