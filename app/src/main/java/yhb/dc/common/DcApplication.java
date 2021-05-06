package yhb.dc.common;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

public class DcApplication extends Application {

    public static Context appContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        appContext = base;
    }
}
