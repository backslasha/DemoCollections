package yhb.dc.demo.demo_fragment.fragment_nest;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import yhb.dc.R;
import yhb.dc.common.LifeCycleActivity;

public class TabFragmentActivity extends LifeCycleActivity implements TabFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container,TabFragment.newInstance("1", "w"), null)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }

    }

    @Override
    protected String getName() {
        return "";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_fragment;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
