package yhb.dc.demo.demo_fragment.fragment_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by yhb on 18-6-12.
 */

public class CustomDialogFragment extends DialogFragment {

    public static CustomDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CustomDialogFragment fragment = new CustomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(new EditText(getActivity()))
                .setCancelable(true)
                .setMessage("Stupid")
                .create();
    }

    /* Note：
     * onCreateDialog 和 onCreateView 方法不能同时返回非 null 实例，否则会报错
     */
}
