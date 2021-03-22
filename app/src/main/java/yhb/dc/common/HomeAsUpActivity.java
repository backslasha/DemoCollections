package yhb.dc.common;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

/**
 * 如果子类布局文件中存在 mToolbar，那么启用 HomeAsUp 功能（返回键结束当前页面）
 * 需要对 mToolbar 进行设置时，重写 setupToolbar(Toolbar mToolbar) 即可
 */
public abstract class HomeAsUpActivity extends DemoBaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar = offerToolbar();

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(isHomeAsUpEnabled());
            }
        }

    }

    protected  Toolbar offerToolbar(){
        return null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();// 模拟 homeAsUp 行为

        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isHomeAsUpEnabled() {
        return true;
    }

}
