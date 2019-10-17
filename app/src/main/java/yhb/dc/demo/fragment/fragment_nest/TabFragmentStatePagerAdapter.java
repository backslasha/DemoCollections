package yhb.dc.demo.fragment.fragment_nest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhb on 18-4-21.
 */

public class TabFragmentStatePagerAdapter extends FragmentPagerAdapter {

    private static final String[] TITLE = {
            "Long title","short","long long title","s","short","Long title","short","long long title","s","short","Long title","short","long long title","s","short"
    };
    private List<NestedFragment> mFragments = new ArrayList<>();

    public TabFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        addFragments();
    }

    private void addFragments() {
        for (int i = 1; i < TITLE.length; i++) {
            mFragments.add(NestedFragment.newInstance(TITLE[i]));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return NestedFragment.newInstance("第 "+position+"个tab");
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
