package yhb.dc.demo.customview.custom_view.debug

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.*
import android.view.View.OnClickListener
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import yhb.dc.R
import yhb.dc.databinding.ViewDebugBinding

class DebugView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {


    companion object {
        val TAG_DEBUG = Object()
    }

    private var mPadding: Rect
    private val mActivity: AppCompatActivity = context as AppCompatActivity
    private lateinit var binding: ViewDebugBinding
    private val mBorder: View by lazy {
        val view = View(context)
        view.tag = TAG_DEBUG
        view.setBackgroundResource(R.drawable.shape_debug_frame)
        view
    }

    @SuppressLint("SetTextI18n")
    private val mOnClickListener = OnClickListener { v ->

        when (v.id) {
            R.id.btn_query -> {
                val name = binding.edtDebug.text.toString()
                val id = resources.getIdentifier(name, "id", getContext().packageName)
                val target = mActivity.findViewById<View>(id)

                if (target == null) {
                    binding.tvDebug.text = "null"
                    return@OnClickListener
                }

                computeDetailInfo(target).let {
                    binding.tvDebug.text = it
                }

                computeLocation(target).let {
                    drawBorder(it[0], it[1], target.width, target.height)
                }
            }

            R.id.btn_toggle -> toggle()
            R.id.btn_clean -> {
                binding.tvDebug.text = null
                if (mBorder.parent != null) {
                    (mBorder.parent as ViewGroup).removeView(mBorder)
                }
            }
        }

    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_debug, this, true)
        binding = ViewDebugBinding.bind(this)
        binding.btnQuery.setOnClickListener(mOnClickListener)
        binding.btnClean.setOnClickListener(mOnClickListener)
        binding.btnToggle.setOnClickListener(mOnClickListener)
        binding.tvDebug.movementMethod = LinkMovementMethod.getInstance()
        mPadding = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
        layoutTransition = LayoutTransition()
        shrinkLayout()
    }

    private fun computeDetailInfo(target: View): SpannableStringBuilder {
        val l = (target.layoutParams as MarginLayoutParams).leftMargin
        val r = (target.layoutParams as MarginLayoutParams).rightMargin
        val t = (target.layoutParams as MarginLayoutParams).topMargin
        val b = (target.layoutParams as MarginLayoutParams).bottomMargin
        val out = computeLocation(target)

        val builder = SpannableStringBuilder()
        builder.append("result=${target.javaClass.simpleName + "(${getIdStr(target)})"}\n")
            .append("parent=${target.parent?.javaClass?.simpleName + "(${getIdStr(target.parent as View)})"}\n")

        builder.append(
            "visibility=${
                when (target.visibility) {
                    View.GONE -> "GONE" + " -> " + "\${VISIBLE}"
                    View.VISIBLE -> "VISIBLE" + " -> " + "\${GONE}"
                    View.INVISIBLE -> "INVISIBLE" + " -> " + "\${VISIBLE}"
                    else -> "UNKNOWN"
                }
            }\n"
        )

        addVisibilitySpan(builder, "\${VISIBLE}") {
            target.visibility = View.VISIBLE
            // 设置完 target 的 visibility 后，直接 addView(mBorder) 的话，成功但是视图没有显示出来，layoutCapture 能看到，
            // 并且 mBorder 的 LayoutParams 信息正常，应该和 layout 的机制有关系，后续验证
            target.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                @SuppressLint("ObsoleteSdkInt")
                override fun onGlobalLayout() {
                    binding.btnQuery.performClick()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        target.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        target.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                }
            })
        }

        addVisibilitySpan(builder, "\${GONE}") {
            target.visibility = View.GONE
            computeDetailInfo(target).let {
                binding.tvDebug.text = it
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

    private fun addVisibilitySpan(
        builder: SpannableStringBuilder,
        spanRegion: String,
        onClick: (() -> Unit)?
    ) {
        val indexOf = builder.indexOf(spanRegion)
        val length = spanRegion.length
        if (indexOf > 0) {
            builder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onClick?.invoke()
                    }
                },
                indexOf,
                indexOf + length,
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }


    private fun computeLocation(target: View): IntArray {
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

    private fun getIdStr(view: View?): String {
        return if (view == null || view.id == View.NO_ID) {
            "no-id"
        } else {
            view.resources.getResourceName(view.id)
        }
    }

    private fun toggle() {
        val closed = binding.groupDebug.visibility == View.GONE
        if (closed) {
            expandLayout()
        } else {
            shrinkLayout()
        }
    }

    private fun shrinkLayout() {
        binding.btnToggle.text = "展开"
        binding.groupDebug.visibility = View.GONE
        val lp = this.layoutParams ?: LayoutParams(0, 0)
        lp.height = WRAP_CONTENT
        lp.width = MATCH_PARENT
        this.layoutParams = lp
        this.setPadding(0, 0, 0, 0)
    }

    private fun expandLayout() {
        binding.btnToggle.text = "折叠"
        binding.groupDebug.visibility = View.VISIBLE
        val lp = this.layoutParams ?: LayoutParams(0, 0)
        lp.height = WRAP_CONTENT
        lp.width = MATCH_PARENT
        this.layoutParams = lp
        this.setPadding(mPadding.left, mPadding.top, mPadding.right, mPadding.bottom)
    }

}
