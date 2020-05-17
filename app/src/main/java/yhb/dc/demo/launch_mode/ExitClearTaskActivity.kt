package yhb.dc.demo.launch_mode

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle

class ExitClearTaskActivity : Activity() {
    companion object {
        fun finishAndRemoveTask(activity: Activity) {
            val intent = Intent(activity, ExitClearTaskActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}