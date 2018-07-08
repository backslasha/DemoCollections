package yhb.dc.demo.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import yhb.dc.R;
import yhb.dc.common.Demo;

public class ServiceHolderActivity extends AppCompatActivity implements Demo {

    private IBookManger mBookManger;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBookManger = BookManagerImpl.asInterface(service);
            mView.setText("STATUE: ALREADY");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mView.setText("STATUE: UNBIND");
        }
    };
    private Button mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servie_holder);
    }

    public void onClick(View view) {
        mView = (Button) view;
        if ("STATUE: UNBIND".equalsIgnoreCase(String.valueOf(mView.getText()))) {
            bindService(new Intent(this, LocalService.class), mConnection, BIND_AUTO_CREATE);
        } else {
            unbindService(mConnection);
        }
    }


}
