package yhb.dc.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import yhb.dc.R;
import yhb.dc.demo.demo_fragment.fragment_nest.TabFragment;

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
