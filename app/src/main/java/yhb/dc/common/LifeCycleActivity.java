package yhb.dc.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yhb on 18-4-21.
 */

public abstract class LifeCycleActivity extends ToolbarActivity {

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        log("onAttachFragment(childFragment): " + childFragment);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

    private void log(String msg) {
        Log.d(this.getClass().getSimpleName(), getName() + ": " + msg);
    }

    abstract protected String getName();
}
