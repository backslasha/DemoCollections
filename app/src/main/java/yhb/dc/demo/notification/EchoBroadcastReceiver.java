package yhb.dc.demo.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class EchoBroadcastReceiver extends BroadcastReceiver {
    public static final String MESSAGE = "MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(MESSAGE);
        if (message != null && !message.isEmpty()) {

            Toast.makeText(context, "Received " + message, Toast.LENGTH_SHORT).show();
        }
    }
}
