package yhb.dc.demo.fragment.fragment_dialog;

import android.content.res.AssetManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.InputStream;

import io.noties.markwon.Markwon;
import yhb.dc.common.DemoBaseActivity;

import static yhb.dc.common.UtilsKt.convertStreamToString;

/**
 * Created by yhb on 18-6-12.
 */

public class ExplainDialog extends DialogFragment {

    private static final String ARG_TEXT = "ARG_TEXT";
    private static final String ARG_PARAM_TYPE = "ARG_PARAM_TYPE";

    private String mData;
    private int mDataType;

    public static ExplainDialog newInstance(String text, int parseType) {
        Bundle args = new Bundle();
        ExplainDialog fragment = new ExplainDialog();
        fragment.setArguments(args);
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_PARAM_TYPE, parseType);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getArguments().getString(ARG_TEXT);
        mDataType = getArguments().getInt(ARG_PARAM_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setPadding(30, 30, 30, 30);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(12);
        textView.setSingleLine(false);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextIsSelectable(true);
        textView.setVerticalScrollBarEnabled(true);
        textView.setHorizontallyScrolling(false);
        textView.setHorizontalScrollBarEnabled(false);
        startRenderText(textView);
        return textView;
    }

    private void startRenderText(TextView textView) {
        String renderText = null;
        if (mDataType == DemoBaseActivity.PARSE_TYPE_TEXT) {
            renderText = mData;
        } else if (mDataType == DemoBaseActivity.PARSE_TYPE_ASSET) {
            final AssetManager am = getContext().getAssets();
            try {
                final InputStream inputStream = am.open(mData);
                renderText = convertStreamToString(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (renderText != null && !renderText.isEmpty()) {
            // https://noties.io/Markwon/docs/v4/core/getting-started.html#quick-one
            final Markwon markwon = Markwon.create(getContext());
            markwon.setMarkdown(textView, renderText);
        }
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
