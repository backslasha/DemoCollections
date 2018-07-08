package yhb.dc.demo.launch_mode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yhb.dc.R;
import yhb.dc.common.LifeCycleActivity;

/**
 * Created by yhb on 18-3-15.
 */

public abstract class LaunchModeBaseActivity extends LifeCycleActivity {
    private TextView mTextView, mTextViewInformation;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviced_intent);
        mTextView = findViewById(R.id.text_view);
        mTextViewInformation = findViewById(R.id.text_view_information);

        mTextViewInformation.setText(getIntent().getStringExtra("LAUNCH_MODE"));

        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mTextView.setText(String.format("%d:\n onNewIntent().", System.currentTimeMillis()));
    }


    @Override
    protected String getName() {
        return this.getClass().getSimpleName();
    }
}
