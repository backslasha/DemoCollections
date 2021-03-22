package yhb.dc.demo.fragment.fragment_dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by yhb on 18-6-12.
 */

public class CustomDialogFragment extends DialogFragment {

    private static final String TAG = CustomDialogFragment.class.getSimpleName();

    public static CustomDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CustomDialogFragment fragment = new CustomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(android.R.layout.activity_list_item, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
//            Window window = dialog.getWindow();
//            if (window != null) {
//                WindowManager.LayoutParams lp = window.getAttributes();
//                lp.gravity = Gravity.BOTTOM;
//                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                window.setAttributes(lp);
//            }
        Log.d(TAG, dialog.findViewById(android.R.id.text1) + "");

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
