package yhb.dc.demo.fragment_nest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yhb.dc.R;
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
