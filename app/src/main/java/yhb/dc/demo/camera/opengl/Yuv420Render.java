package yhb.dc.demo.camera.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Yuv420Render implements GLSurfaceView.Renderer {

    private final Context context;

    private YUV420Texture yuv420Texture;

    public Yuv420Render(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        yuv420Texture = new YUV420Texture(context);
        yuv420Texture.initYUV();
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //宽高
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清空颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //设置背景颜色
//        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        final long realtime = SystemClock.elapsedRealtime();
        yuv420Texture.draw();
    }

    public void setYuvData(int width, int height, byte[] y, byte[] u, byte[] v) {
        if (yuv420Texture != null) {
            yuv420Texture.setYUVData(width, height, y, u, v);
        }
    }
}
