package yhb.dc.demo.fragment.fragment_nest;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;
import yhb.dc.common.LifeCycleFragment;

public class NestedFragment extends LifeCycleFragment {

    private static final String ARGUMENT_MESSAGE = "MESSAGE";

    public static NestedFragment newInstance(String message) {
        NestedFragment fragment = new NestedFragment();
        Bundle args = new Bundle();
        args.putString(NestedFragment.ARGUMENT_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    private int i = 0;
    protected String mName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), i++ + "", Toast.LENGTH_SHORT).show();
        mName = getName();
    }

    @Override
    public String getName() {
        if (mName == null) {
            if (getArguments() != null) {
                mName = getArguments().getString(NestedFragment.ARGUMENT_MESSAGE);
            }
        }
        return mName + "("+this.hashCode()+")";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LogRecyclerView logRecyclerView = new LogRecyclerView(getActivity());
        logRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        logRecyclerView.setAdapter(new CommonAdapter<String>(android.R.layout.simple_list_item_1, list()) {
            @Override
            public void convert(CommonViewHolder holder, String entity) {
                ((TextView) holder.itemView).setText(entity);
            }
        });
        return logRecyclerView;
    }

    private List<String> list() {
        List<String> result = new ArrayList<>();
        for (int j = 0; j < 30; j++) {
            result.add("String" + j);
        }
        return result;
    }

}

