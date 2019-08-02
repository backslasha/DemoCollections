package yhb.dc.common

import android.support.v7.app.AppCompatActivity
import yhb.dc.demo.fragment.fragment_dialog.ExplainDialog
import yhb.dc.demo.fragment.fragment_dialog.WebDialog

abstract class DemoBaseActivity : AppCompatActivity() {

    open val TAG: String by lazy {
        javaClass.simpleName
    }

    open fun descriptionUrl(): String? {
        return null
    }

    open fun descriptionText(): String? {
        return null
    }

    open fun openDescription() {
        descriptionUrl()?.let {
            WebDialog.newInstance(it)
                    .show(supportFragmentManager, "WebDialog")
            return
        }

        descriptionText()?.let {
            ExplainDialog.newInstance(it)
                    .show(supportFragmentManager, "ExplainDialog")
        }

    }

}
