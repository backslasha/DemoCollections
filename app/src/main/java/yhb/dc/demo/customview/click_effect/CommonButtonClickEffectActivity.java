package yhb.dc.demo.customview.click_effect;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import yhb.dc.R;
import yhb.dc.common.Demo;

public class CommonButtonClickEffectActivity extends AppCompatActivity  implements Demo {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_button_click_effect);

        final ImageView imageView = findViewById(R.id.image_button_animated_vector_drawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((AnimatedVectorDrawable) imageView.getDrawable()).start();
                }
            }
        });
    }
}
