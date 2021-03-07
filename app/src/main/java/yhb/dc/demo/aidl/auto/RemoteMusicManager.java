package yhb.dc.demo.aidl.auto;

import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * created by yaohaibiao on 2021/3/7.
 */
public class RemoteMusicManager extends IMusicManager.Stub {

    private static final String TAG = "RemoteMusicManager";
    private final List<Music> mMusicList = new ArrayList<>();

    {
        mMusicList.add(new Music("Hi ketty", 0));
        mMusicList.add(new Music("Old friend", 1));
        mMusicList.add(new Music("Beautiful girl", 2));
    }

    @Override
    public void addMusic(Music music) throws RemoteException {
        if (mMusicList.contains(music)) {
            return;
        }
        Log.i(TAG, "addMusic=" + music + ", current thread=" + Thread.currentThread().getName());
        int i = 0;
        while (i++ < 3) {
            try {
                Thread.sleep(1000);
                Log.i(TAG, "try add music " + i + "...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        music.musicId++;
        mMusicList.add(music);
    }

    @Override
    public List<Music> getMusicList() throws RemoteException {
        int i = 0;
        Log.i(TAG, "getMusicList, current thread=" + Thread.currentThread().getName());
        while (i-- > 0) {
            try {
                Thread.sleep(1000);
                Log.i(TAG, "try get music list " + i + "...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return mMusicList;
    }
}
