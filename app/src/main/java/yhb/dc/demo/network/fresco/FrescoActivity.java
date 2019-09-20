package yhb.dc.demo.network.fresco;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;
import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;

public class FrescoActivity extends AppCompatActivity  {

    private static int index = 0;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fresco, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    public void onClick(View view) {
        int currentItem = mViewPager.getCurrentItem();
        PlaceholderFragment currentFragment = (PlaceholderFragment) mSectionsPagerAdapter.getItem(currentItem);
        currentFragment.loadMore10Item();
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView mRecyclerView;
        private CommonAdapter<String> mCommonAdapter;


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fresco, container, false);

            mCommonAdapter = new CommonAdapter<String>(R.layout.item_image_card, new ArrayList<String>()) {
                @Override
                public void convert(CommonViewHolder holder, String entity) {
                    SimpleDraweeView imageView = holder.itemView.findViewById(R.id.iv_poster);
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(entity))
                            .setResizeOptions(new ResizeOptions(50, 50))
                            .build();
                    imageView.setController(
                            Fresco.newDraweeControllerBuilder()
                                    .setOldController(imageView.getController())
                                    .setImageRequest(request)
                                    .build());
                }
            };

            mRecyclerView = rootView.findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {

            });
            mRecyclerView.setAdapter(mCommonAdapter);
            loadMore10Item();
            return rootView;
        }

        public void loadMore10Item() {
            List<String> uris = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                if (i == 0) {
                    uris.add("http://img.like.video/asia_live/4h2/0Dlg1t.png");
                } else {
                    uris.add("https://bing.ioliu.cn/v1?w=1200&h=1920&d=" + index++);
                }
            }
            mCommonAdapter.notifyDataSetChanged(uris, CommonAdapter.Op.APPEND);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                mFragments.add(PlaceholderFragment.newInstance(i));
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
    }
}
