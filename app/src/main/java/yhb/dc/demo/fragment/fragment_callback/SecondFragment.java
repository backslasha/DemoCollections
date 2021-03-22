package yhb.dc.demo.fragment.fragment_callback;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import yhb.dc.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yhb on 18-5-16.
 */

public class SecondFragment extends Fragment {
    public static SecondFragment newInstance() {

        Bundle args = new Bundle();

        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Button mButton1, mButton2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_second, container, false);
        mButton1 = inflate.findViewById(R.id.button1);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("DATA", "mButton1");
                Toast.makeText(getActivity(), "mButton1", Toast.LENGTH_SHORT).show();
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                getFragmentManager().popBackStack();
            }
        });
        mButton2 = inflate.findViewById(R.id.button2);
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("DATA", "mButton2");
                Toast.makeText(getActivity(), "mButton2", Toast.LENGTH_SHORT).show();
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                getFragmentManager().popBackStack();
            }
        });
        return inflate;
    }
}

