package yhb.dc.demo.slient_upgrade;

import java.io.File;

/**
 * apk下载监听
 * Created by barryjiang on 2015/12/15.
 */
public interface UpdateAPKDownloadListener {

    //存储空间不足
    int ERROR_NOT_ENOUGH_STORAGE                = -3;
    int ERROR_CODE_ALREADY_DOWNLOADING          = -2;
    int ERROR_CODE_ORTHER                       = -1;

    void onDownloadProgress(double progress);
    void onDownloadSuccess(File file);
    void onDownloadFailed(Throwable e, int errCode);
}