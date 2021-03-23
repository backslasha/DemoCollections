package yhb.dc.demo.camera;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, "camera is not available");
        }
        return c;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
        try {
            changePreviewSize(mCamera, getWidth(), getHeight());
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(this);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.i(TAG, "processing frame");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头

    public void toggleCamera() throws IOException {
        //切换前后摄像头
        final SurfaceHolder holder = mHolder;
        final CameraInfo cameraInfo = new CameraInfo();
        final int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.setPreviewCallback(null);
                    mCamera.setPreviewDisplay(null);
                    mCamera.release();//释放资源
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setDisplayOrientation(90);
                        mCamera.setPreviewDisplay(holder);//通过 surfaceView 显示取景画面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.setPreviewCallback(null);
                    mCamera.setPreviewDisplay(null);
                    mCamera.release();//释放资源
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setDisplayOrientation(90);
                        mCamera.setPreviewDisplay(holder);//通过 surfaceView 显示取景画面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览
                    cameraPosition = 1;
                    break;
                }
            }

        }
    }

    /**
     * 修改相机的预览尺寸，调用此方法就行
     *
     * @param camera     相机实例
     * @param viewWidth  预览的 surfaceView 的宽
     * @param viewHeight 预览的 surfaceView 的高
     */
    protected void changePreviewSize(Camera camera, int viewWidth, int viewHeight) {
        if (camera == null) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        Camera.Size closelySize = null;//储存最合适的尺寸
        for (Camera.Size size : sizeList) { //先查找preview中是否存在与 surfaceView 相同宽高的尺寸
            if ((size.width == viewWidth) && (size.height == viewHeight)) {
                closelySize = size;
            }
        }
        if (closelySize == null) {
            // 得到与传入的宽高比最接近的size
            float reqRatio = ((float) viewWidth) / viewHeight;
            float curRatio, deltaRatio;
            float deltaRatioMin = Float.MAX_VALUE;
            for (Camera.Size size : sizeList) {
                if (size.width < 1024) continue;//1024表示可接受的最小尺寸，否则图像会很模糊，可以随意修改
                curRatio = ((float) size.width) / size.height;
                deltaRatio = Math.abs(reqRatio - curRatio);
                if (deltaRatio < deltaRatioMin) {
                    deltaRatioMin = deltaRatio;
                    closelySize = size;
                }
            }
        }
        if (closelySize != null) {
            Log.i("changePreviewSize", "预览尺寸修改为：" + closelySize.width + "*" + closelySize.height);
            parameters.setPreviewSize(closelySize.width, closelySize.height);
            camera.setParameters(parameters);
        }
    }
}