package yhb.dc.demo.customview.custom_view.debug;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.common.DemoBaseActivity;

public class DebugActivity extends DemoBaseActivity implements Demo {

    private StringBuilder mBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        findViewById(R.id.tv_standard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugActivity.this.onClick(v);
            }
        });
    }

    public void onClick(View view) {

        traverseLevelly(findViewById(android.R.id.content), new Function2<View, Integer, Void>() {
            @Override
            public Void invoke(View view, Integer level) {
                mBuilder.append(String.format("%s", view.getClass().getSimpleName() + "(" + getId(view) + ")" + ", "));
                return null;
            }
        }, new Function1<Integer, Void>() {
            @Override
            public Void invoke(Integer level) {
                if (level == 0) {
                    return null;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < level; i++) {
                    builder.append("--");
                }
                mBuilder.append("\n").append(builder.toString());
                return null;
            }
        });
        openDescription();
        mBuilder = new StringBuilder();




    }


    private void traverse(View root) {
        if (root == null) {
            return;
        }

        Log.d(getTAG(), root.getClass().getSimpleName() + " getZ()=" + root.getTranslationZ());
        Log.d(getTAG(), root.getClass().getSimpleName() + " getZ()=" + root.getZ());

        if (root instanceof ViewGroup) {
            int childCount = ((ViewGroup) root).getChildCount();
            for (int i = 0; i < childCount; i++) {
                traverse(((ViewGroup) root).getChildAt(i));
            }
        }
    }

    /**
     * @return "[package]:id/[xml-id]"
     * where [package] is your package and [xml-id] is id of view
     * or "no-id" if there is no id
     */
    public static String getId(View view) {
        if (view.getId() == View.NO_ID) return "no-id";
        else return view.getResources().getResourceName(view.getId());
    }


    private void traverseLevelly(View root, Function2<View, Integer, Void> function1, Function1<Integer, Void> function0) {

        if (root == null) {
            return;
        }

        LinkedList<View> queue0 = new LinkedList<>();
        LinkedList<View> queue1 = new LinkedList<>();

        queue0.add(root);

        int level = 0;

        while (true) {
            function0.invoke(level);
            while (!queue0.isEmpty()) {
                View remove = queue0.remove(0);
                function1.invoke(remove, level);
                if (remove instanceof ViewGroup) {
                    int childCount = ((ViewGroup) remove).getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        queue1.add(((ViewGroup) remove).getChildAt(i));
                    }
                }
            }
            level++;
            if (queue1.isEmpty()) {
                break;
            }
            queue0.addAll(queue1);
            queue1.clear();
        }
    }

    @Nullable
    @Override
    public String descriptionText() {
        return mBuilder.toString();
    }
}

