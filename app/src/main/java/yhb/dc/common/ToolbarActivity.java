package yhb.dc.common;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import yhb.dc.R;

/**
 * 如果子类布局文件中存在 toolbar，那么 enable HomeAsUp 功能
 * 需要对 toolbar 进行设置时，重写 setupToolbar(Toolbar toolbar) 即可
 */
public abstract class ToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (-1 != getContentViewId()) {
            setContentView(getContentViewId());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setupToolbar(toolbar);
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setupToolbar(Toolbar toolbar) {

    }

    public abstract int getContentViewId();

}
