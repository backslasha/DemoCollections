package yhb.dc.demo.slient_upgrade;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import yhb.dc.R;

public class SilentUpgradeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slient_upgrade);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case -1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已授予外部储存读写权限！", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void startDownloadService(View view) {

    }

    public void deleteApkIfExist(View view) {

    }

    public class DownloadIntentService extends IntentService {

        private final static String DEFAULT_APK_TMP_SUFFIX = ".tmp";
        private final static String APK_FILE_SUFFIX = ".apk";

        private String mDownloadUrl;
        private String mDownloadFilePath;
        private UpdateAPKDownloadListener mUpdateAPKDownloadListener;

        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public DownloadIntentService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {

            File downloadFile = new File(mDownloadFilePath);

            if (!downloadFile.exists()) {
                try {
                    downloadFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }

            try {
                URL url = new URL(mDownloadUrl);
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                double fileLength = httpURLConnection.getContentLength();

                BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());


                OutputStream out = new FileOutputStream(mDownloadFilePath);
                int size = 0;
                double len = 0;
                byte[] buf = new byte[1024];
                while ((size = bin.read(buf)) != -1) {
                    len += size;
                    out.write(buf, 0, size);

                    if (mUpdateAPKDownloadListener != null) {
                        mUpdateAPKDownloadListener.onDownloadProgress(len / fileLength);
                    }
                }

                //修改文件后缀名
                String parentFilePath = downloadFile.getParent();
                String apkFileName = downloadFile.getName().replaceFirst(DEFAULT_APK_TMP_SUFFIX, APK_FILE_SUFFIX);
                File apkFile = new File(parentFilePath, apkFileName);
                if (downloadFile.renameTo(apkFile)) {
                    if (mUpdateAPKDownloadListener != null) {
                        mUpdateAPKDownloadListener.onDownloadSuccess(apkFile);
                    }
                } else {
                    if (mUpdateAPKDownloadListener != null) {
                        mUpdateAPKDownloadListener.onDownloadFailed(null, UpdateAPKDownloadListener.ERROR_CODE_ORTHER);
                    }
                }

                bin.close();
                out.close();
            } catch (IOException e) {

                if (mUpdateAPKDownloadListener != null) {
                    mUpdateAPKDownloadListener.onDownloadFailed(e, UpdateAPKDownloadListener.ERROR_CODE_ORTHER);
                }
            }
        }

        public void setUpdateAPKDownloadListener(UpdateAPKDownloadListener mUpdateAPKDownloadListener) {
            this.mUpdateAPKDownloadListener = mUpdateAPKDownloadListener;
        }
    }


}
