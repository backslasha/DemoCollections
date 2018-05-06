package yhb.dc.demo.fragment_nest;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import yhb.dc.R;

public class SingleFragmentActivity extends AppCompatActivity implements TabFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
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
    public void onFragmentInteraction(Uri uri) {

    }
}
