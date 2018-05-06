package yhb.dc.demo.tranistion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import yhb.dc.R;
import yhb.dc.common.ToolbarActivity;

public class AnimationMainActivity extends ToolbarActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_transition;
    }


    @Override
    public void onClick(View v) {

    }
}
