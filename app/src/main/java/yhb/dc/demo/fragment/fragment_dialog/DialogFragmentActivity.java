package yhb.dc.demo.fragment.fragment_dialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import yhb.dc.R;
import yhb.dc.common.Demo;

@Demo(id = Demo.DEMO_ID_BOTTOM_DIALOG_BUG, name = "BottomDialog 闪跳问题处理")
public class DialogFragmentActivity extends AppCompatActivity   {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);
    }

    public void callDialog(View view) {
        BottomDialogFixed.newInstance().show(getSupportFragmentManager(), "dialog");
    }
    public void callDialog0(View view) {
        BottomDialogWithBug.newInstance().show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        Log.d("BottomDialogFixed", "onEnterAnimationComplete");
    }
}
