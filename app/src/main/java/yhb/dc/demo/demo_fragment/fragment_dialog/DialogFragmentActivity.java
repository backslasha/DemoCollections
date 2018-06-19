package yhb.dc.demo.demo_fragment.fragment_dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import yhb.dc.R;

public class DialogFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);
    }

    public void callDialog(View view) {
        CustomDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
    }
}
