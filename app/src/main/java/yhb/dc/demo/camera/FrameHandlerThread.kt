package yhb.dc.demo.camera

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.os.SystemClock
import android.util.Log
import yhb.dc.demo.camera.opengl.Yuv420SurfaceView
import java.lang.ref.WeakReference

/**
 * created by yaohaibiao on 2021/3/29.
 */
class FrameHandlerThread(yuv420SurfaceView: Yuv420SurfaceView) :
        HandlerThread("FrameHandlerThread") {

    private val yuv420SurfaceViewRef = WeakReference<Yuv420SurfaceView>(yuv420SurfaceView)
    private val identifier = SystemClock.elapsedRealtime().toInt()

    private lateinit var handler: Handler

    override fun start() {
        super.start()
        handler = object : Handler(looper) {
            override fun handleMessage(msg: Message) {
                if (msg.what != identifier) {
                    return
                }
                val surfaceView = yuv420SurfaceViewRef.get() ?: return
                val frameHandler = msg.obj as FrameHandler?
                frameHandler?.handle(surfaceView)
            }
        }
    }

    fun enqueue(image: ByteArray?, format: Int, width: Int, height: Int, rotation: Int,
                usingFrontCamera: Boolean) {
        if (!isAlive) {
            Log.w(Nv21Collector.TAG, "enqueue while handler thread is not alive.")
            return
        }
        if (handler.hasMessages(identifier)) {
            handler.removeMessages(identifier)
            Log.w(Nv21Collector.TAG, "drop frame!")
        }
        handler.sendMessage(Message.obtain().apply {
            what = identifier
            obj = FrameHandler(image, format, width, height, rotation, usingFrontCamera)
        })
    }

    companion object {
        private var rotateBuffer: ByteArray? = null

        private fun rotateBuffer(size: Int): ByteArray? {
            if (rotateBuffer != null && rotateBuffer!!.size == size) {
                return rotateBuffer
            }
            rotateBuffer = ByteArray(size)
            return rotateBuffer
        }
    }


    class FrameHandler(
            private val nv21Data: ByteArray?,
            private val format: Int,
            private val width: Int,
            private val height: Int,
            private val rotation: Int,
            private val usingFrontCamera: Boolean
    ) {

        fun handle(yuv420SurfaceView: Yuv420SurfaceView) {

//            Log.i("yaohaibiaoDebug", "frameArrived")
//            var elapsedRealtime = SystemClock.elapsedRealtime()
//            BeautyOnlyApp.arController.onDrawFrame(
//                    nv21Data,
//                    format,
//                    width,
//                    height,
//                    rotation
//            ) // 使用独立美颜接口处理 nv21 数据
//            DebugInfoCollector.frameSdkHandledCost(SystemClock.elapsedRealtime() - elapsedRealtime)
//            Log.i("yaohaibiaoDebug", "frameSdkHandled")
//
//            elapsedRealtime = SystemClock.elapsedRealtime()
//            val needRotateAngel = when {
//                usingFrontCamera && rotation == 90 -> 270
//                usingFrontCamera && rotation == 270 -> 90
//                else -> rotation
//            }
            nv21Data ?: return
            val buffer = rotateBuffer(nv21Data.size)
            FrameFormatUtils.NV21ToNV12(nv21Data, buffer, width, height)
            var exchangedWH = false
            val handleResult = when (rotation) {
                90 -> {
                    FrameFormatUtils.rotateYUV420Degree90(buffer, width, height).also {
                        exchangedWH = true
                    }
                }
                180 -> FrameFormatUtils.rotateYUV420Degree180(buffer, width, height)
                270 -> {
                    FrameFormatUtils.rotateYUV420Degree270(buffer, width, height).also {
                        exchangedWH = true
                    }
                }
                else -> buffer
            }
            yuv420SurfaceView.setYUVData(if (exchangedWH) height else width, if (exchangedWH) width else height, handleResult)
        }
    }

}