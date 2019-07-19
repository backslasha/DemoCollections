package yhb.dc.demo.activity

import android.os.Bundle
import android.view.View

import yhb.dc.R
import yhb.dc.common.Demo
import yhb.dc.common.LifeCycleActivity
import yhb.dc.demo.fragment.fragment_dialog.ExplainDialog
import yhb.dc.demo.fragment.fragment_dialog.WebDialog

class TranslucentActivity : LifeCycleActivity(), Demo {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)
    }

    fun onClick(view: View) {
//        ExplainDialog.newInstance("""
//<style name="Transparent" parent="AppTheme">
//    <item name="android:windowBackground">@android:color/transparent</item>
//    <item name="android:windowNoTitle">true</item>
//    <item name="android:windowIsTranslucent">true</item>
//    <item name="android:windowAnimationStyle">@android:style/Animation.Translucent</item>
//</style>
//                """).show(supportFragmentManager, "ExplainDialog")
        openDescription()

    }

    override fun descriptionUrl(): String? {
        return "https://app.yinxiang.com/shard/s59/nl/22472101/c2080bb5-95a3-4f16-835a-73f115f7a655"
    }

}
