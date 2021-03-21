package yhb.dc.demo.launch_mode

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_clear_task_activty.*
import yhb.dc.R
import yhb.dc.common.Demo
import yhb.dc.common.DemoBaseActivity

@Demo(id = Demo.DEMO_ID_CLEAR_TASK, name = "清空任务栈")
class ClearTaskDemo : DemoBaseActivity() {

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
        setContentView(R.layout.activity_clear_task_activty)
        btn_exit_app.setOnClickListener { ExitClearTaskActivity.finishAndRemoveTask(this@ClearTaskDemo) }
        btn_exit_app2.setOnClickListener { this@ClearTaskDemo.finishAndRemoveTask() }
        btn_launch_new.setOnClickListener {
            startActivity(Intent(this@ClearTaskDemo, this@ClearTaskDemo.javaClass)
                    .putExtra(KEY_INFO, num + 1))
        }
        tv_content.text = "I'm the $num Activity."
    }

    override fun descriptionData(): String? {
        return "document/clearTaskDemo.md"
    }

    override fun descriptionType(): Int {
        return PARSE_TYPE_ASSET
    }
}

