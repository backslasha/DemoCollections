package yhb.dc.demo.fragment_nest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhb on 18-4-21.
 */

public class TabFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<NestedFragment> mFragments = new ArrayList<>();

    public TabFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        addFragments();
    }

    private void addFragments() {
        for (int i = 1; i <= 4; i++) {
            mFragments.add(NestedFragment.newInstance("第 " + i + " 个 Fragment"));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getName();
    }
}
