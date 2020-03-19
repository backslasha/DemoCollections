package yhb.dc.demo.fragment.fragment_callback;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import yhb.dc.R;
import yhb.dc.common.Demo;

@Demo
public class FragmentCallbackActivity extends AppCompatActivity   {
    private Fragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_callback);
        if (null == getSupportFragmentManager().findFragmentById(R.id.container)) {
            mMainFragment = createFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mMainFragment)
                    .commitNow();
        }
    }

    private Fragment createFragment() {
        return MainFragment.newInstance();
    }

}
