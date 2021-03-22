package yhb.dc.demo.fragment.fragment_callback;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import yhb.dc.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yhb on 18-5-16.
 */

public class MainFragment extends Fragment {

    private TextView mTextView;
    private Button mButton;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final int REQUEST_CODE_CHOOSE_A_COLOR_FOR_ADDED = 1;
    private static final int REQUEST_CODE_CHOOSE_A_COLOR_FOR_EDITED = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        mTextView = inflate.findViewById(R.id.text_view);
        mButton = inflate.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondFragment secondFragment = SecondFragment.newInstance();
                secondFragment.setTargetFragment(MainFragment.this, REQUEST_CODE_CHOOSE_A_COLOR_FOR_EDITED);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, secondFragment)
                        .addToBackStack(null)
                        .setCustomAnimations(
                                R.anim.switch_in_top,
                                R.anim.switch_in_bottom,
                                R.anim.switch_in_top,
                                R.anim.switch_in_bottom
                        )
                        .commit();
                mList.clear();

            }
        });
        return inflate;
    }

    ArrayList<String> mList = new ArrayList<>(1);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && (requestCode == REQUEST_CODE_CHOOSE_A_COLOR_FOR_ADDED
                || requestCode == REQUEST_CODE_CHOOSE_A_COLOR_FOR_EDITED)) {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String data1 = data.getStringExtra("DATA");
            mList.add(data1);

            mTextView.setText(mList.get(0));

            SparseArray<Integer> arrayList = new SparseArray<>(10);
            for (int i = 0; i < 11; i++) {
                arrayList.put(i,i);
            }
            System.out.println("Finish: "+arrayList);
        }
    }

}
