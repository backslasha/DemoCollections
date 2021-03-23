package yhb.dc.demo.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanmeraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonStartPreview.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return@setOnClickListener
            }
            startPreview()
        }
        binding.buttonStopPreview.setOnClickListener {
            stopPreview()
        }
        binding.buttonSwitchCamera.setOnClickListener {
            mPreview?.toggleCamera()
        }
    }

    private var mPreview: CameraPreview? = null
    private fun startPreview() {
        mPreview = CameraPreview(this)
        binding.cameraPreview.addView(mPreview)
//        SettingsFragment.passCamera(mPreview.getCameraInstance())
//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
//        SettingsFragment.setDefault(PreferenceManager.getDefaultSharedPreferences(this))
//        SettingsFragment.init(PreferenceManager.getDefaultSharedPreferences(this))
//        binding.buttonSettings.setOnClickListener {
//            fragmentManager.beginTransaction().replace(R.id.camera_preview, SettingsFragment()).addToBackStack(null).commit()
//        }
    }

    private fun stopPreview() {
        binding.cameraPreview.removeAllViews()
    }
}