package yhb.dc.demo.demo_view.custom_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.common.HomeAsUpActivity;

/**
 * Created by yhb on 18-3-15.
 */

public class CustomViewMainActivity extends AppCompatActivity implements Demo {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
    }
}
