@file:Suppress("DEPRECATION")

package yhb.dc.demo.camera

import android.app.Activity
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.Camera.PreviewCallback
import android.util.Log
import yhb.dc.common.DcApplication
import java.io.IOException


class Nv21Collector(defaultCamera: CameraPosition) : PreviewCallback {

    private var camera: Camera? = null
    var cameraPosition = defaultCamera
        private set
    private var cameraId = -1
    private var cameraInfo: Camera.CameraInfo? = null

    // 用来设置给 camera 使其回调 onPreviewFrame
    // 不能设置成匿名内部类，否则回调几帧便停止了，具体原因未知
    private val dummySurfaceTexture = SurfaceTexture(100)

    var onNv21Arrived: ((ByteArray, Int, Int, Int, Int, Boolean) -> Unit)? = null
    var onPreviewSizeChanged: ((Camera.Size) -> Unit)? = null

    enum class CameraPosition {
        FRONT, BACK
    }

    companion object {
        const val TAG = "CameraPreview"
        const val TAG_TIME_MEASURE = "time_measure"
        private val viewWidth = ScreenUtil.getScreenWidth(DcApplication.appContext)
        private val viewHeight = ScreenUtil.getScreenHeight(DcApplication.appContext)
    }

    fun startCollect(force: Boolean = false) {
        if (!force && camera != null) {
            Log.w(TAG, "startPreview while previewing.")
            return
        }
        val result = openCommonCamera()
        result.second?.let {
            setParams(it)
            startCollect(it)
            cameraInfo = result.first
        }
        Log.i(TAG, "startPreview cameraPosition=$cameraPosition，rotation=${cameraInfo?.orientation}")
    }

    private fun openCommonCamera(): Pair<Camera.CameraInfo, Camera?> {
        val cameraId = if (cameraPosition == CameraPosition.FRONT) {
            Camera.CameraInfo.CAMERA_FACING_FRONT
        } else {
            Camera.CameraInfo.CAMERA_FACING_BACK
        }
        val numberOfCameras = Camera.getNumberOfCameras()
        var cameraOpened: Camera? = null
        val cameraInfo = Camera.CameraInfo()
        for (i in 0 until numberOfCameras) {
            Camera.getCameraInfo(i, cameraInfo) //得到每一个摄像头的信息
            if (cameraInfo.facing == cameraId) {
                cameraOpened = Camera.open(i)
                this.cameraId = i
                break
            }
        }
        return cameraInfo to cameraOpened
    }

    private fun setParams(camera: Camera) {
        val preViewSize = CameraUtils.getOptimalPreviewSize(camera, viewWidth, viewHeight) ?: return
        Log.i(TAG, "setParams: preViewSize width " + preViewSize.width + " height " + preViewSize.height)
        val parameters: Camera.Parameters = camera.parameters
        parameters.previewFormat = ImageFormat.NV21
        parameters["orientation"] = "portrait"
        parameters.setPreviewSize(preViewSize.width, preViewSize.height)
        val previewFps = 30
        onPreviewSizeChanged?.invoke(preViewSize)
        val range = CameraUtils.adaptPreviewFps(previewFps, parameters.supportedPreviewFpsRange)
        parameters.setPreviewFpsRange(range[0], range[1])
        Log.i(TAG, "setPreviewFpsRange: preferFps=${previewFps * 1000}, resultFps= ${range[0]} ~ ${range[1]}")
        camera.parameters = parameters
    }

    private fun startCollect(camera: Camera) {
        try {
            stopCollect()
            camera.setPreviewCallback(this)
            // 不实际预览到屏幕上
            // camera.setPreviewDisplay(holder)
            camera.setPreviewTexture(dummySurfaceTexture)
            camera.startPreview()
            Log.i(TAG, "supported formats=${camera.parameters.supportedPreviewFormats}")
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: " + e.message)
        } finally {
            this.camera = camera
        }
    }

    fun stopCollect() {
        camera?.setPreviewCallback(null)
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    fun toggleCamera() {
        // 切换前后摄像头
        cameraPosition = if (cameraPosition == CameraPosition.FRONT) {
            CameraPosition.BACK
        } else {
            CameraPosition.FRONT
        }
        startCollect(true)
    }

    override fun onPreviewFrame(nv21Data: ByteArray, camera: Camera) {
        val parameters = this.camera?.parameters ?: return
        val previewSize = parameters.previewSize
        val orientation = cameraInfo?.orientation ?: return
        onNv21Arrived?.invoke(
                nv21Data,
                1,
                previewSize.width,
                previewSize.height,
                orientation,
                cameraPosition == CameraPosition.FRONT
        )
    }

    fun cameraOrientation(): Int {
        return cameraInfo?.orientation?:0
    }

}