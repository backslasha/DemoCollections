package yhb.dc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;
import yhb.dc.common.Demo;
import yhb.dc.common.LifeCycleActivity;

public class MainActivity extends LifeCycleActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Class<? extends Activity>> activities = new ArrayList<>();

        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    this.getPackageName(), PackageManager.GET_ACTIVITIES
            );
            for (ActivityInfo activityInfo : packageInfo.activities) {
                Class<? extends Activity> activityClass = (Class<? extends Activity>) Class.forName(activityInfo.name);
                if (isDemo(activityClass))
                    activities.add(activityClass);
            }

        } catch (PackageManager.NameNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        }

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


    private boolean isDemo(Class<? extends Activity> activityClass) {
        Class<?>[] interfaces = activityClass.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (Demo.class.equals(anInterface)) {
                return true;
            }
        }
        return false;
    }

}
