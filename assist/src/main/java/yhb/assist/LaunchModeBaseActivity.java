package yhb.assist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by yhb on 18-3-15.
 */

public class LaunchModeBaseActivity extends Activity {
    private TextView mTextView, mTextViewInformation;
    private Button mButton;
    private Button mButtonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviced_intent);
        mTextView = findViewById(R.id.text_view);
        mTextViewInformation = findViewById(R.id.text_view_information);

        mTextViewInformation.setText(getIntent().getStringExtra("LAUNCH_MODE"));

        mButton = findViewById(R.id.button);
        mButtonExit = findViewById(R.id.exit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = newIntent(LaunchModeBaseActivity.this);
                startActivity(intent);
            }
        });
        mButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitClearTaskActivity.Companion.finishAndRemoveTask(LaunchModeBaseActivity.this);
            }
        });
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LaunchModeBaseActivity.class);
        intent.putExtra("LAUNCH_MODE", "Standard");
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mTextView.setText(String.format("%d:\n onNewIntent().", System.currentTimeMillis()));
    }


    protected String getName() {
        return this.getClass().getSimpleName();
    }
}
