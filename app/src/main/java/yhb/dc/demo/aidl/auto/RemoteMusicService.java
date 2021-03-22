package yhb.dc.demo.aidl.auto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

/**
 * Created by yhb on 18-4-24.
 */

public class RemoteMusicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RemoteMusicManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(RemoteMusicManager.class.getSimpleName(), "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }
}
