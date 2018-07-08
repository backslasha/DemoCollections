package yhb.dc.demo.tranistion;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.common.HomeAsUpActivity;

public class AnimationMainActivity extends HomeAsUpActivity implements View.OnClickListener, Demo {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

    }

    @Override
    protected Toolbar offerToolbar() {
        return null;
    }


    @Override
    public void onClick(View v) {

    }
}
