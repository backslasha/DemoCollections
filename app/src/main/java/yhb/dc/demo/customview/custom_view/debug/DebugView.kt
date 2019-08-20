package yhb.dc.demo.customview.custom_view.debug

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.view_debug.view.*
import yhb.dc.R
import android.view.View as View1


class DebugView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {


    companion object {
        val TAG_DEBUG = Object()
    }

    private var mPadding: Rect
    private val mActivity: AppCompatActivity = context as AppCompatActivity
    private val mBorder: android.view.View by lazy {
        val view = android.view.View(context)
        view.tag = TAG_DEBUG
        view.setBackgroundResource(R.drawable.shape_frame)
        view
    }

    @SuppressLint("SetTextI18n")
    private val mOnClickListener = OnClickListener { v ->

        when (v.id) {
            R.id.btn_query -> {
                val name = edt_debug.text.toString()
                val id = resources.getIdentifier(name, "id", getContext().packageName)
                val target = mActivity.findViewById<View1>(id)

                if (target == null) {
                    tv_debug.text = "null"
                    return@OnClickListener
                }

                computeDetailInfo(target).let {
                    tv_debug.text = it
                }

                computeLocation(target).let {
                    drawBorder(it[0], it[1], target.width, target.height)
                }
            }

            R.id.btn_toggle -> toggle()
            R.id.btn_clean -> {
                tv_debug.text = null
                if (mBorder.parent != null) {
                    (mBorder.parent as ViewGroup).removeView(mBorder)
                }
            }
        }

    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_debug, this, true)
        btn_query.setOnClickListener(mOnClickListener)
        btn_clean.setOnClickListener(mOnClickListener)
        btn_toggle.setOnClickListener(mOnClickListener)
        tv_debug.movementMethod = LinkMovementMethod.getInstance()
        setBackgroundColor(resources.getColor(R.color.dark_translucent))
        mPadding = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
        layoutTransition = LayoutTransition()

        btn_toggle.tag = "true" == (btn_toggle.tag as String).toLowerCase()
        val statusExpanded = btn_toggle.tag as Boolean
        if (statusExpanded) {
            expandLayout()
        } else {
            shrinkLayout()
        }
    }

    private fun computeDetailInfo(target: android.view.View): SpannableStringBuilder {
        val l = (target.layoutParams as MarginLayoutParams).leftMargin
        val r = (target.layoutParams as MarginLayoutParams).rightMargin
        val t = (target.layoutParams as MarginLayoutParams).topMargin
        val b = (target.layoutParams as MarginLayoutParams).bottomMargin
        val out = computeLocation(target)

        val builder = SpannableStringBuilder()
        builder.append("result=${target.javaClass.simpleName + "(${getIdStr(target)})"}\n")
                .append("parent=${target.parent?.javaClass?.simpleName + "(${getIdStr(target.parent as android.view.View)})"}\n")

        builder.append("visibility=${when (target.visibility) {
            View1.GONE -> "GONE" + " -> " + "\${VISIBLE}"
            View1.VISIBLE -> "VISIBLE" + " -> " + "\${GONE}"
            View1.INVISIBLE -> "INVISIBLE" + " -> " + "\${VISIBLE}"
            else -> "UNKNOWN"
        }}\n")


        addVisibilitySpan(builder, "\${VISIBLE}") {
            Toast.makeText(context, "set target visible!", Toast.LENGTH_SHORT).show()
            target.visibility = android.view.View.VISIBLE
            computeDetailInfo(target).let {
                tv_debug.text = it
            }
            computeLocation(target).let {
                drawBorder(it[0], it[1], target.width, target.height)
            }
        }

        addVisibilitySpan(builder, "\${GONE}") {
            Toast.makeText(context, "set target gone!", Toast.LENGTH_SHORT).show()
            target.visibility = android.view.View.GONE
            computeDetailInfo(target).let {
                tv_debug.text = it
            }
            computeLocation(target).let {
                drawBorder(it[0], it[1], target.width, target.height)
            }
        }


        builder.append("margin(l,t,r,b)=$l,$t,$r,$b,\n")
                .append("padding(l,t,r,b)=(${target.paddingLeft},${target.paddingTop},${target.paddingRight},${target.paddingBottom})\n")
                .append("width=${target.width}\n")
                .append("height=${target.height}\n")
                .append("relative_x=${target.x}\n")
                .append("relative_y=${target.y}\n")
                .append("absolute_x=${out[0]}\n")
                .append("absolute_y=${out[1]}\n")

        return builder

    }

    private fun addVisibilitySpan(builder: SpannableStringBuilder, spanRegion: String, onClick: (() -> Unit)?) {
        val indexOf = builder.indexOf(spanRegion)
        val length = spanRegion.length
        if (indexOf > 0) {
            builder.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: android.view.View?) {
                            onClick?.invoke()
                        }
                    },
                    indexOf,
                    indexOf + length,
                    SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }


    private fun computeLocation(target: android.view.View): IntArray {
        val out = IntArray(2)
        target.getLocationOnScreen(out)
        return out
    }


    private fun drawBorder(x: Int, y: Int, width: Int, height: Int) {
        if (mBorder.parent != null) {
            (mBorder.parent as ViewGroup).removeView(mBorder)
        }
        (mActivity.window?.decorView as ViewGroup).let {
            val lp = FrameLayout.LayoutParams(width, height)
            lp.topMargin = y
            lp.leftMargin = x
            it.addView(mBorder, lp)
        }
    }

    private fun getIdStr(view: android.view.View?): String {
        return if (view == null || view.id == View1.NO_ID) {
            "no-id"
        } else {
            view.resources.getResourceName(view.id)
        }
    }

    private fun toggle() {
        val closed = !(btn_toggle.tag as Boolean)
        if (closed) {
            expandLayout()
            btn_toggle.tag = true
        } else {
            shrinkLayout()
            btn_toggle.tag = false
        }
    }

    private fun shrinkLayout() {
        btn_toggle.text = "展开"
        group_debug.visibility = android.view.View.GONE
        this.layoutParams = this.layoutParams ?: LayoutParams(0, 0)
        this.layoutParams.height = WRAP_CONTENT
        this.layoutParams.width = WRAP_CONTENT
        this.layoutParams = this.layoutParams
        this.setPadding(0, 0, 0, 0)
    }

    private fun expandLayout() {
        btn_toggle.text = "折叠"
        group_debug.visibility = android.view.View.VISIBLE
        this.layoutParams = this.layoutParams ?: LayoutParams(0, 0)
        this.layoutParams.height = MATCH_PARENT
        this.layoutParams.width = MATCH_PARENT
        this.layoutParams = this.layoutParams
        this.setPadding(mPadding.left, mPadding.top, mPadding.right, mPadding.bottom)
    }

}
