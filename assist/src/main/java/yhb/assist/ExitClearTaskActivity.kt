package yhb.assist

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class ExitClearTaskActivity : Activity() {
    companion object {
       
        fun finishAndRemoveTask(context: Activity) {
            val intent = Intent(context, ExitClearTaskActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}