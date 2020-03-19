package yhb.dc.demo.handler_thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import yhb.dc.R;
import yhb.dc.common.Demo;

@Demo
public class HandlerThreadActivity extends AppCompatActivity   {

    private TextView textView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);
        HandlerThread handlerThread = new HandlerThread("Second-Handler-Thread");
        handlerThread.start();

        textView = new TextView(HandlerThreadActivity.this);
        textView.setText(String.valueOf(1));
        mHandler = new Handler(handlerThread.getLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                WindowManager windowManager = getWindowManager();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                        50,
                        50,
                        300,
                        300,
                        WindowManager.LayoutParams.TYPE_APPLICATION,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW
                );
                windowManager.addView(textView, layoutParams);
            }
        });

    }

    public void onClick(View view) {


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
                mHandler.postDelayed(this, 1000);
            }
        };

        mHandler.postDelayed(runnable, 1000);

        MessageQueue queue = mHandler.getLooper().getQueue();

    }
}
