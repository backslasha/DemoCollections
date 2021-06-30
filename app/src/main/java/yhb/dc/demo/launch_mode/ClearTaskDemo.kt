package yhb.dc.demo.launch_mode

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import yhb.dc.MainActivity
import yhb.dc.common.Demo
import yhb.dc.common.DemoBaseActivity
import yhb.dc.databinding.ActivityClearTaskActivtyBinding

@Demo(id = Demo.DEMO_ID_CLEAR_TASK, name = "清空任务栈")
class ClearTaskDemo : DemoBaseActivity() {

    private lateinit var binding: ActivityClearTaskActivtyBinding

    companion object {
        const val KEY_INFO = "key_info"
    }

    private val num: Int by lazy {
        intent.getIntExtra(KEY_INFO, 0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClearTaskActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnExitApp.setOnClickListener { ExitClearTaskActivity.finishAndRemoveTask(this@ClearTaskDemo) }
        binding.btnExitApp2.setOnClickListener { this@ClearTaskDemo.finishAndRemoveTask() }
        binding.btnExitApp3.setOnClickListener {
            val get = MainActivity.mainRef?.get()
            if (get == null) {
                Toast.makeText(this, "MainActivity not found.", Toast.LENGTH_SHORT).show()
            } else {
                get.finish()
                Toast.makeText(this, "MainActivity finished.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnLaunchNew.setOnClickListener {
            startActivity(
                Intent(this@ClearTaskDemo, this@ClearTaskDemo.javaClass)
                    .putExtra(KEY_INFO, num + 1)
            )
        }
        binding.tvContent.text = "I'm the $num Activity."
    }

    override fun descriptionData(): String? {
        return "document/clearTaskDemo.md"
    }

    override fun descriptionType(): Int {
        return PARSE_TYPE_ASSET
    }
}

