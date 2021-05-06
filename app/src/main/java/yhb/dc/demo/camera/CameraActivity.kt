package yhb.dc.demo.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import yhb.dc.R
import yhb.dc.common.Demo
import yhb.dc.common.DemoBaseActivity
import yhb.dc.databinding.ActivityCanmeraBinding
import yhb.dc.demo.camera.opengl.Yuv420SurfaceView


/**
 * created by yaohaibiao on 2021/3/22.
 */
@Demo(id = Demo.DEMO_ID_CAMERA, name = "相机预览、旋转逻辑")
class CameraActivity : DemoBaseActivity() {

    private lateinit var binding: ActivityCanmeraBinding

    private lateinit var mNv21Collector: Nv21Collector
    private lateinit var yuv420SurfaceView: Yuv420SurfaceView
    private var frameHandlerThread: FrameHandlerThread? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanmeraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonStartPreview.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return@setOnClickListener
            }
            mNv21Collector.startCollect()
        }
        binding.buttonStopPreview.setOnClickListener {
            mNv21Collector.stopCollect()
        }
        binding.buttonSwitchCamera.setOnClickListener {
            mNv21Collector.toggleCamera()
        }
        binding.buttonSettings.setOnClickListener {
            mRotation += 90
            mRotation %= 360
            yuv420SurfaceView.setWH(yuv420SurfaceView.height, yuv420SurfaceView.width)
            binding.buttonSettings.text = "渲染顺时针旋转(${mRotation}度)"
            Log.i(TAG, "mRotation changed to $mRotation, height=${yuv420SurfaceView.height}, width=${yuv420SurfaceView.width} ")
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.statusBarColor = Color.TRANSPARENT

        yuv420SurfaceView = findViewById(R.id.yuv420_surface_view)
        mNv21Collector = Nv21Collector(savedInstanceState?.getSerializable(KEY_CAMERA_POSITION) as Nv21Collector.CameraPosition?
                ?: Nv21Collector.CameraPosition.FRONT)
        mNv21Collector.onPreviewSizeChanged = object : (android.hardware.Camera.Size) -> Unit {
            override fun invoke(previewSize: android.hardware.Camera.Size) {
                val previewWidth = previewSize.width
                val previewHeight = previewSize.height
                yuv420SurfaceView.setWH(previewWidth,previewHeight)
            }
        }
        mNv21Collector.onNv21Arrived = object : ((ByteArray, Int, Int, Int, Int, Boolean) -> Unit) {
            override fun invoke(image: ByteArray, format: Int, width: Int, height: Int,
                                rotation: Int, usingFrontCamera: Boolean) {
                frameHandlerThread?.enqueue(image, format, width, height, mRotation, usingFrontCamera)
            }
        }
    }

    private fun updateText() {
        val degrees =  when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 ->  270
            else -> 0
        }
        val stringBuilder = StringBuilder()
        val cameraOrientation = mNv21Collector.cameraOrientation()
        if (mNv21Collector.cameraPosition == Nv21Collector.CameraPosition.FRONT) {
            stringBuilder.append("facing front, result(${(cameraOrientation + degrees) % 360}) = (info.orientation($cameraOrientation) + degrees($degrees)) % 360;")
        }else{
            stringBuilder.append("facing back, result(${(cameraOrientation - degrees + 360) % 360}) = (info.orientation($cameraOrientation) - degrees($degrees) + 360) % 360;")
        }
        binding.tvDesc.text = stringBuilder.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_CAMERA_POSITION, mNv21Collector.cameraPosition)
    }

    private fun Yuv420SurfaceView.setWH(width: Int, height: Int) {
        val layoutParams = layoutParams as ViewGroup.MarginLayoutParams?
                ?: FrameLayout.LayoutParams(0, 0)
        this.layoutParams = layoutParams.apply {
            this.height = height
            this.width = width
        }
    }

    private var mRotation = 0

    companion object {
        private const val TAG = "CameraActivity"
        private const val KEY_CAMERA_POSITION = "key_camera_position"
    }

    override fun onStart() {
        super.onStart()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return
        }
        mNv21Collector.startCollect()
        updateText()
        frameHandlerThread = FrameHandlerThread(yuv420SurfaceView).apply {
            start()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mNv21Collector.startCollect()
            updateText()
            frameHandlerThread = FrameHandlerThread(yuv420SurfaceView).apply {
                start()
            }
        } else {
            Toast.makeText(this, "Please grant the permissions!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        mNv21Collector.stopCollect()
        frameHandlerThread?.quit()
        frameHandlerThread = null
    }
}