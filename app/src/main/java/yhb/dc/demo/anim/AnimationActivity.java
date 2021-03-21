package yhb.dc.demo.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.common.DemoBaseActivity;

public class AnimationActivity extends DemoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        final ImageView button = findViewById(R.id.button);
//        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
//        shapeDrawable.getPaint().setStrokeWidth(dp2px(this, 5f));
//        shapeDrawable.getPaint().setColor(Color.BLUE);
//        int padding = (int) (shapeDrawable.getPaint().getStrokeWidth() / 2);
//        shapeDrawable.setPadding(padding,padding,padding,padding);
//        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
//        shapeDrawable.setIntrinsicHeight(button.getHeight());
//        shapeDrawable.setIntrinsicWidth(button.getWidth());
//        button.setBackground(shapeDrawable);


    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }
}
