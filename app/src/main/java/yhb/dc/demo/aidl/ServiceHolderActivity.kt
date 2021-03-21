package yhb.dc.demo.aidl

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import yhb.dc.R
import yhb.dc.common.Demo
import yhb.dc.common.DemoBaseActivity
import yhb.dc.demo.aidl.auto.IMusicManager
import yhb.dc.demo.aidl.auto.Music
import yhb.dc.demo.aidl.auto.RemoteMusicService

@Demo(id = Demo.DEMO_ID_AIDL_USAGE, name = "AIDL 使用规则与疑点")
@SuppressLint("SetTextI18n")
class ServiceHolderActivity : DemoBaseActivity() {

    private var mMusicManager: IMusicManager? = null
    private var mButton02: Button? = null
    private var mTextView: TextView? = null
    private val mMusicConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mMusicManager = IMusicManager.Stub.asInterface(service)
            mButton02!!.text = "STATUE(Music): ALREADY"
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mButton02!!.text = "STATUE(Music): UNBIND"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servie_holder)
        mTextView = findViewById(R.id.tv_logger)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMusicManager?.let {
            unbindService(mMusicConnection)
        }
    }

    fun onClick02(view: View?) {
        mButton02 = view as Button?
        if (mButton02!!.text.toString().contains("UNBIND")) {
            bindService(Intent(this, RemoteMusicService::class.java), mMusicConnection, Context.BIND_AUTO_CREATE)
        } else {
            try {
                val candidate = Music("candidate", 3)
                log("before candidate=$candidate")
                mMusicManager!!.addMusic(candidate)
                log("after candidate=$candidate")
                log("thread=" + Thread.currentThread().name + ",mMusicManager.getMusicList=" + mMusicManager!!.musicList)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    private fun log(text: String) {
        Log.i(TAG, text)
        mTextView!!.append("\n")
        mTextView!!.append(text)
    }

    override fun descriptionText(): String? {
        return description
    }

    companion object {
        private const val TAG = "ServiceHolderActivity"
        private const val description = """
            <h1> aidl 疑问点梳理</h1>
            <li> oneway 修饰符。通常情况下，客户端发起一个远程调用，在调用回来之前，客户端调用线程处于阻塞状态，如果是 UI 进程则界面会完全卡住。
        使用 oneway 修饰后，客户端则仅仅是向服务端发送数据参数数据后，立即返回，避免了线程意外阻塞。oneway 修饰符使用有两个限制条件，即修饰的方法
        必须没有返回值，参数必须没有用 out 进行修饰，因为这两种场景下，客户端都依赖了调用的返回才能继续往下执行。
            <li> 服务端在收到客户端的远程调用时，由系统调度一个运行在服务端进程的 Binder 线程池线程进行处理；同个客户端发起的多个 oneway 调用会
        按照其调用顺序进行依次返回。
            <li>定义 aidl 接口时，参数前的 in、out、inout 表示数据流向，基本类型参数无法修饰，强制为 in 类型；
        非基本数据类型的必须使用其中一个进行修饰；
            <li>in 修饰参数时，server 端能接受此参数的所有字段，仅用 out 修饰的非基本类型数据对像，客户端对其做的任何修改，服务端都感知不到，
        表现为服务端接收到一个空白的参数。例如客户端调用 addMusic 添加 <code>Music{name='candidate', musicId=3}</code>，服务端接受到此参数
        时，读出来确是  <code>Music{name='null', musicId=0}</code>
            <li>out 修饰参数时，server 端对该参数做的任何修改，客户端均能感知到。例如服务端在接收到客户端的 <code>Music{name='null', musicId=0}</code>
        后，将其 musicId 重新赋值成 1，即 <code>Music{name='null', musicId=1}，而后客户端传递过来的该对像实例，musicId 也会同步被设置成 1。这个设置
        操作显然也是跨进程的一个同步，因此势必会阻塞到客户端调用线程，这也解释了为何使用了 oneway 修饰符修饰接口，则无法使用 out 修饰其参数（编译失败）
        
            
"""
    }
}