package yhb.dc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;
import yhb.dc.demo.animated_vector.CommonButtonClickEffectActivity;
import yhb.dc.demo.animation.AnimationMainActivity;
import yhb.dc.demo.custom_view.CustomViewMainActivity;
import yhb.dc.demo.launch_mode.LaunchModeMainActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Class<? extends Activity>> activities = new ArrayList<>();
        activities.addAll(Arrays.asList(
                LaunchModeMainActivity.class,
                CustomViewMainActivity.class,
                AnimationMainActivity.class,
                CommonButtonClickEffectActivity.class
        ));

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new CommonAdapter<Class<? extends Activity>>(R.layout.item_activity, activities) {
            @Override
            public void convert(CommonViewHolder holder, final Class<? extends Activity> entity) {
                holder.setText(R.id.text_view_activity_name, entity.getSimpleName());
                holder.bindOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, entity);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
