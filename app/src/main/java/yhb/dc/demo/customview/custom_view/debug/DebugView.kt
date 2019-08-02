package yhb.dc.demo.customview.custom_view.debug

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.view_debug.view.*
import yhb.dc.R

class DebugView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val mActivity: AppCompatActivity = context as AppCompatActivity

    private val mOnClickListener = OnClickListener { v ->
        if (v.id == R.id.btn_debug) {
            val name = edt_debug.text.toString()
            val id = resources.getIdentifier(name, "id", getContext().packageName)
            val target = mActivity.findViewById<View>(id)
            if (target == null) {
                Toast.makeText(mActivity, "当前 Activity 找不到这个 ID 的控件！", Toast.LENGTH_SHORT).show()
            } else {
                val l = (target.layoutParams as MarginLayoutParams).leftMargin
                val r = (target.layoutParams as MarginLayoutParams).rightMargin
                val t = (target.layoutParams as MarginLayoutParams).topMargin
                val b = (target.layoutParams as MarginLayoutParams).bottomMargin

                val out = IntArray(2)
                target.getLocationOnScreen(out)

                tv_debug.text = String.format(("ID=$name\n" +
                        "margin(l,t,r,b)=$l,$t,$r,$b,\n" +
                        "padding(l,t,r,b)=(${target.paddingLeft},${target.paddingTop},${target.paddingRight},${target.paddingBottom})\n" +
                        "width=${target.width}\n" +
                        "height=${target.height}\n" +
                        "relative_x=${target.x}\n" +
                        "relative_y=${target.y}\n" +
                        "absolute_x=${out[0]}\n" +
                        "absolute_y=${out[1]}\n" +
                        "").trimIndent())

            }
        }
    }

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_debug, this, true)
        btn_debug.setOnClickListener(mOnClickListener)
    }

}
