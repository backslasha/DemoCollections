package yhb.dc.demo.animated_vector;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import yhb.dc.R;

public class CommonButtonClickEffectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_button_click_effect);

        final ImageView imageView = findViewById(R.id.image_button_animated_vector_drawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AnimatedVectorDrawable) imageView.getDrawable()).start();
            }
        });
    }
}
