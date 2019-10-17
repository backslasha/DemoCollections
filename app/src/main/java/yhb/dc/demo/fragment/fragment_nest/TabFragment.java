package yhb.dc.demo.fragment.fragment_nest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yhb.dc.R;
import yhb.dc.common.LifeCycleFragment;

public class TabFragment extends LifeCycleFragment {

    private OnFragmentInteractionListener mListener;
    private PagerAdapter mAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_tab, container, false);
        mViewPager = inflate.findViewById(R.id.view_pager);
        mTabLayout = inflate.findViewById(R.id.tab_layout);
        setupViewPager(mViewPager);
        setupTabLayout(mTabLayout);
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupTabLayout(TabLayout tabLayout) {
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tabAt = tabLayout.getTabAt(i);
            if (tabAt != null) {
                tabAt.setText(mAdapter.getPageTitle(i));
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new TabFragmentStatePagerAdapter(getFragmentManager());
        viewPager.setAdapter(mAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected String getName() {
        return this.getClass().getSimpleName();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
