package yhb.dc.demo.camera.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.util.Arrays;

public class Yuv420SurfaceView extends GLSurfaceView {

    private final Yuv420Render mYuv420Render;

    public Yuv420SurfaceView(Context context) {
        this(context, null);
    }

    public Yuv420SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mYuv420Render = new Yuv420Render(context);
        setRenderer(mYuv420Render);
        //mode=GLSurfaceView.RENDERMODE_WHEN_DIRTY之后  调用requestRender()触发Render的onDrawFrame函数
        //mode=GLSurfaceView.RENDERMODE_CONTINUOUSLY之后  自动调用onDrawFrame  60fps左右
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setYUVData(int width, int height, byte[] y, byte[] u, byte[] v) {
        if (mYuv420Render != null) {
            mYuv420Render.setYuvData(width, height, y, u, v);
            requestRender();
        }
    }

    public void setYUVData(int width, int height, byte[] yuv) {
        final byte[] y = extractY(yuv, width, height);
        final byte[] u = extractU(yuv, width, height);
        final byte[] v = extractV(yuv, width, height);
        if (mYuv420Render != null) {
            mYuv420Render.setYuvData(width, height, y, u, v);
            requestRender();
        }
    }

    private byte[] extractY(byte[] yuv, int width, int height) {
        return Arrays.copyOf(yuv, width * height);
    }

    private byte[] extractU(byte[] yuv, int width, int height) {
        final byte[] u = new byte[width * height / 4];
        System.arraycopy(yuv, width * height, u, 0, (width * height) / 4);
        return u;
    }

    private byte[] extractV(byte[] yuv, int width, int height) {
        final byte[] v = new byte[width * height / 4];
        System.arraycopy(yuv, width * height * 5 / 4, v, 0, (width * height) / 4);
        return v;
    }
}
