package yhb.dc.demo.animation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import yhb.dc.R;
import yhb.dc.common.ToolbarActivity;

public class AnimationMainActivity extends ToolbarActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((Button) findViewById(R.id.button_1)).requestLayout();

        new Thread() {
            @Override
            public void run() {
                ((Button) findViewById(R.id.button_1)).setText(String.valueOf(getWindow().isActive()));
            }
        }.start();

    }


    @Override
    protected void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run() {
                ((Button) findViewById(R.id.button_2)).setText("onStart()");
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();



        new Thread() {
            @Override
            public void run() {
                ((Button) findViewById(R.id.button_3)).setText("onResume()");
            }
        }.start();
    }



    @Override
    public int getContentViewId() {
        return R.layout.activity_4_button_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                break;
            case R.id.button_2:
                break;
            case R.id.button_3:
                break;

        }
    }
}
