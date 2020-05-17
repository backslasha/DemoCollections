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

@Demo(autoJumpIn = true)
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

    override fun descriptionText(): String? {
        return description
    }
}

private const val description = """
    <h1>退出应用程序的方法</h1>
    <li>留意到 android 5.0 之后 Activity 有一个 <code>finishAndRemoveTask()</code> 的接口，此接口会结束当前 Activity，
    当且仅当该 Activity 为应用程序栈最后一个 Activity 时，此方法会将对应的任务栈从系统的"最近应用程序列表"中移除</li>
    <li>在 android 5.0 之前，没有找到将任务栈从"最近应用程序列表"中移除的方法</li>
    <li>若希望退出所有 Activity，可以使用 <code>Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK</code> 启动
    一个透明的 Activity 并立即结束它来实现，需要注意的是，Intent.FLAG_ACTIVITY_CLEAR_TASK 清空的 Task 指的是和此透明 Activity Affinity 
    相同的任务栈，在多任务栈的情况下要特别注意此 case.
    <li>一个任务栈的 Affinity 等于它最底下的 Activity Manifest 中定义的 Affinity，不指定时默认为 packageName
"""
