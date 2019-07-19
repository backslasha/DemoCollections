package yhb.dc.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by yhb on 18-4-21.
 * 继承此类的的 Fragment 会自动打印 log
 */

public abstract class LifeCycleActivity extends DemoBaseActivity {

    protected String TAG;

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        log("onAttachFragment(childFragment): " + childFragment);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TAG = getName();
        super.onCreate(savedInstanceState);
        log("onCreate(savedInstanceState): " + savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        log("onSaveInstanceState(outState): " + outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause();)");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("onStop()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestroy()");
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        log("onAttachedToWindow()");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        log("onDetachedFromWindow()");
    }

    private void log(String msg) {
        Log.d(this.getClass().getSimpleName(), getName() + ": " + msg);
    }

    protected String getName() {
        return this.getClass().getCanonicalName();
    }
}
