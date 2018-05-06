package yhb.dc.demo.tab_view_pager_fragment_nest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yhb.dc.R;

public class NestedFragment extends LifeCycleFragment {

    private static final String ARGUMENT_MESSAGE = "MESSAGE";

    public static NestedFragment newInstance(String message) {
        NestedFragment fragment = new NestedFragment();
        Bundle args = new Bundle();
        args.putString(NestedFragment.ARGUMENT_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    protected String mName;

    @Override
    public String getName() {
        if (mName == null) {
            if (getArguments() != null) {
                mName = getArguments().getString(NestedFragment.ARGUMENT_MESSAGE);
            }
        }
        return mName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_nested, container, false);
        ((TextView) inflate).setText(getName());
        return inflate;
    }


}
