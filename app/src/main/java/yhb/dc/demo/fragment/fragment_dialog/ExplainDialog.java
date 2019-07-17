package yhb.dc.demo.fragment.fragment_dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by yhb on 18-6-12.
 */

public class ExplainDialog extends DialogFragment {

    private static final String ARG_TEXT = "ARG_TEXT";
    private String mText;

    public static ExplainDialog newInstance(String text) {
        Bundle args = new Bundle();
        ExplainDialog fragment = new ExplainDialog();
        fragment.setArguments(args);
        args.putString(ARG_TEXT, text);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mText = getArguments().getString(ARG_TEXT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setPadding(100, 100, 100, 100);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(mText);
        textView.setTextSize(12);
        textView.setTextIsSelectable(true);
        return textView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

}
