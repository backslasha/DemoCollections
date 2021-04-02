package yhb.dc.demo.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import yhb.dc.common.Demo
import yhb.dc.common.DemoBaseActivity
import yhb.dc.databinding.ActivityCanmeraBinding


/**
 * created by yaohaibiao on 2021/3/22.
 */
@Demo(id = Demo.DEMO_ID_ACTIVITY_LAUNCH_MODE, name = "相机预览")
class CameraActivity : DemoBaseActivity() {

    private lateinit var binding: ActivityCanmeraBinding
    private lateinit var previewView: CameraPreview

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
            previewView.startPreview()
        }
        binding.buttonStopPreview.setOnClickListener {
            previewView.stopPreview()
        }
        binding.buttonSwitchCamera.setOnClickListener {
            previewView.toggleCamera()
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.statusBarColor = Color.TRANSPARENT
        previewView =  CameraPreview(this)
        binding.cameraPreview.addView(previewView)
    }

    companion object{
        private const val TAG = "CameraActivity"
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return
        }
        previewView.startPreview()
    }

    override fun onPause() {
        super.onPause()
        previewView.stopPreview()
    }
}