package yhb.dc.demo.camera

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.PreviewCallback
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException
import kotlin.math.abs

class CameraPreview(context: Context?) : SurfaceView(context), SurfaceHolder.Callback, PreviewCallback {

    private var camera: Camera? = null

    companion object {
        private const val TAG = "CameraPreview"
    }

    init {
        holder.addCallback(this)
    }

    fun startPreview() {
        Log.i(TAG, "startPreview usingForegroundCamera=$usingForegroundCamera")
        getCamera(usingForegroundCamera)?.let {
            startPreview(it)
        }
    }

    private fun startPreview(camera: Camera) {
        try {
            stopPreview()
            changePreviewSize(camera, width, height)
            camera.setPreviewCallback(this)
            camera.setPreviewDisplay(holder)
            camera.setDisplayOrientation(90)
            camera.startPreview()
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: " + e.message)
        } finally {
            this.camera = camera
        }
    }

    fun stopPreview() {
        camera?.setPreviewCallback(null)
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.i(TAG, "surfaceCreated")
        camera?.setPreviewDisplay(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.i(TAG, "surfaceDestroyed")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        Log.i(TAG, "surfaceChanged")
    }

    override fun onPreviewFrame(data: ByteArray, camera: Camera) {
//        Log.i(TAG, "onPreviewFrame")
    }

    private var usingForegroundCamera = true //0代表前置摄像头，1代表后置摄像头

    fun toggleCamera() {
        usingForegroundCamera = !usingForegroundCamera//切换前后摄像头
        startPreview()
    }

    private fun getCamera(foreground: Boolean): Camera? {
        val cameraInfo = CameraInfo()
        val cameraCount = Camera.getNumberOfCameras() //得到摄像头的个数
        for (i in 0 until cameraCount) {
            Camera.getCameraInfo(i, cameraInfo) //得到每一个摄像头的信息
            if (foreground && cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                return Camera.open(i)
            } else if (!foreground && cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                return Camera.open(i)
            }
        }
        return null
    }

    /**
     * 修改相机的预览尺寸，调用此方法就行
     *
     * @param camera     相机实例
     * @param viewWidth  预览的 surfaceView 的宽
     * @param viewHeight 预览的 surfaceView 的高
     */
    private fun changePreviewSize(camera: Camera?, viewWidth: Int, viewHeight: Int) {
        camera ?: return
        val closelySize = closelyPreviewSize(viewWidth, viewHeight)
        val parameters = camera.parameters
        if (closelySize != null && parameters != null) {
            parameters.setPreviewSize(closelySize.width, closelySize.height)
            camera.parameters = parameters
            Log.i("changePreviewSize", "预览尺寸修改为：" + closelySize.width + "*" + closelySize.height)
        }
    }

    private fun closelyPreviewSize(viewWidth: Int, viewHeight: Int): Camera.Size? {
        val camera = camera ?: return null
        val parameters = camera.parameters
        val sizeList = parameters.supportedPreviewSizes
        var closelySize: Camera.Size? = null //储存最合适的尺寸
        for (size in sizeList) { //先查找preview中是否存在与 surfaceView 相同宽高的尺寸
            if (size.width == viewWidth && size.height == viewHeight) {
                closelySize = size
            }
        }
        if (closelySize == null) {
            // 得到与传入的宽高比最接近的size
            val reqRatio = viewWidth.toFloat() / viewHeight
            var curRatio: Float
            var deltaRatio: Float
            var deltaRatioMin: Float = Float.MAX_VALUE
            for (size in sizeList) {
                if (size.width < 1024) continue  //1024表示可接受的最小尺寸，否则图像会很模糊，可以随意修改
                curRatio = size.width.toFloat() / size.height
                deltaRatio = abs(reqRatio - curRatio)
                if (deltaRatio < deltaRatioMin) {
                    deltaRatioMin = deltaRatio
                    closelySize = size
                }
            }
        }
        return closelySize
    }

}