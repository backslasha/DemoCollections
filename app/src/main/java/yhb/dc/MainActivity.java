package yhb.dc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;
import yhb.dc.common.Demo;
import yhb.dc.common.LifeCycleActivity;

public class MainActivity extends LifeCycleActivity {

    private static final int debuggingDemoId = -1;

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
            Class autoJumpTo = null;
            for (ActivityInfo activityInfo : packageInfo.activities) {
                @SuppressWarnings("unchecked") Class<? extends Activity> activityClass
                        = (Class<? extends Activity>) Class.forName(activityInfo.name);
                Demo annotation = activityClass.getAnnotation(Demo.class);
                if (annotation == null) {
                    continue;
                }
                activities.add(activityClass);
                if (annotation.id() == debuggingDemoId) {
                    autoJumpTo = activityClass;
                }
            }

            if (autoJumpTo != null) {
                Intent intent = new Intent(this, autoJumpTo);
                startActivity(intent);
            }

        } catch (PackageManager.NameNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CommonAdapter<Class<? extends Activity>>(R.layout.item_activity, activities) {
            @Override
            public void convert(CommonViewHolder holder, final Class<? extends Activity> entity) {
                final Demo annotation = entity.getAnnotation(Demo.class);
                final String specifiedName = annotation == null ? null : annotation.name();
                holder.setText(R.id.text_view_activity_name, specifiedName == null || specifiedName.isEmpty()
                        ? entity.getSimpleName() : specifiedName);
                holder.bindOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, entity);
                    startActivity(intent);
                });
            }
        });
    }


}
