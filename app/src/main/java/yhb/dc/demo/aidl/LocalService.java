package yhb.dc.demo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by yhb on 18-4-24.
 */

public class LocalService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BookManagerImpl();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(BookManagerImpl.class.getSimpleName(), "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }
}
