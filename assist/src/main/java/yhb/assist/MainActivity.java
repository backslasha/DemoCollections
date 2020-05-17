package yhb.assist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_single_task).setOnClickListener(this);
        findViewById(R.id.button_single_instance).setOnClickListener(this);
        findViewById(R.id.button_standard).setOnClickListener(this);
        findViewById(R.id.shutdown).setOnClickListener(this);
        findViewById(R.id.button_single_top).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        switch (v.getId()) {
            case R.id.button_single_instance:
                intent.setAction("yhb.dc.SINGLEINSTANCE");
                intent.putExtra("LAUNCH_MODE", "SINGLE_INSTANCE");
                startActivity(intent);
                break;
            case R.id.button_single_task:
                intent.setAction("yhb.dc.SINGLETASK");
                intent.putExtra("LAUNCH_MODE", "SINGLE_TASK");
                startActivity(intent);
                break;
            case R.id.button_standard:
                startActivity(LaunchModeBaseActivity.newIntent(this));
                break;
            case R.id.button_single_top:
                Intent intent1 = new Intent(this, LaunchModeBaseActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            case R.id.shutdown:
                ExitClearTaskActivity.Companion.finishAndRemoveTask(this);
                break;
        }
    }
}
