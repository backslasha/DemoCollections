package yhb.dc.demo.customview.custom_view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import yhb.dc.R
import yhb.dc.common.Demo

@Demo(id = Demo.DEMO_ID_CUSTOM_VIEW, name = "自定义 View 练习")
class CustomViewDisplayActivity : AppCompatActivity() {

    private val spinner by lazy {
        this.findViewById<Spinner>(R.id.spinner)
    }

    private var views: ArrayList<View> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view_display)
        val root = (window.decorView.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

        views.addAll(getCustomViewList())

        val viewNames = ArrayList<String>()
        for (view in views) {
            viewNames.add(view.javaClass.simpleName)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewNames).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinner.apply {

            this.isSelected = false
            this.adapter = adapter
            this.onItemSelectedListener = object : OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    if (root.childCount == 2) {
                        root.removeView(root.getChildAt(1))
                    }
                    root.addView(this@CustomViewDisplayActivity.views[position])
                    Toast.makeText(this@CustomViewDisplayActivity, "selected $position", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun getCustomViewList(): Collection<View> {
        val views = ArrayList<View>()
        views.add(ScaleIndicator(context = this).apply { ScaleIndicator.debugSetup(this) })
        return views
    }
}
