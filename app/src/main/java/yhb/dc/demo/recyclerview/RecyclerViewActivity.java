package yhb.dc.demo.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yhb.dc.R;
import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;
import yhb.dc.common.Demo;
import yhb.dc.common.HomeAsUpActivity;

public class RecyclerViewActivity extends HomeAsUpActivity implements View.OnClickListener, Demo {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new CommonAdapter<String>(android.R.layout.simple_list_item_1, getStringList()) {
            @Override
            public void convert(CommonViewHolder holder, String entity) {
                ((TextView) holder.itemView).setText(entity);
                holder.itemView.setBackgroundResource(R.drawable.shape_inset);

            }
        });
        recyclerView.setLayoutManager(new WiredLayoutManager());
    }

    private List<String> getStringList() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            strings.add("第" + i + "条数据");
        }
        return strings;
    }


    @Override
    public void onClick(View v) {

    }
}
