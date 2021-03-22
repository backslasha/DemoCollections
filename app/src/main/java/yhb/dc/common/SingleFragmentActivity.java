package yhb.dc.common;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import yhb.dc.R;
import yhb.dc.demo.fragment.fragment_nest.TabFragment;

/**
 * 单 Fragment 布局的 Activity 可以继承此类
 * 1. 无需额外写布局
 * 2. 重写方法 {@link #createFragment()} 即可，Fragment 会在 onCreate 时被创建
 */
public abstract class SingleFragmentActivity extends AppCompatActivity implements TabFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container, createFragment())
                    .commit();
        }

    }

    protected abstract Fragment createFragment();

}
