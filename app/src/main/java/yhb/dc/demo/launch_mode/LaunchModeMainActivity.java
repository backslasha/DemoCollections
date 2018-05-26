package yhb.dc.demo.launch_mode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import yhb.dc.R;
import yhb.dc.common.LifeCycleActivity;
import yhb.dc.common.ToolbarActivity;

public class LaunchModeMainActivity extends LifeCycleActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.button_single_task).setOnClickListener(this);
        findViewById(R.id.button_single_instance).setOnClickListener(this);
        findViewById(R.id.button_standard).setOnClickListener(this);
        findViewById(R.id.button_single_top).setOnClickListener(this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_launch_mode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_single_task:
                Intent intent = SingleTaskActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.button_single_instance:
                intent = SingleInstanceActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.button_standard:
                 intent = StandardActivity.newIntent(this);
                startActivity(intent);
                break;
            case R.id.button_single_top:
                intent = SingleTopBaseActivity.newIntent(this);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected String getName() {
        return this.getClass().getSimpleName();
    }
}
