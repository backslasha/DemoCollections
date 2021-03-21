package yhb.dc.common

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.ViewDragHelper
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import yhb.dc.R
import yhb.dc.demo.fragment.fragment_dialog.ExplainDialog
import yhb.dc.demo.fragment.fragment_dialog.WebDialog


abstract class DemoBaseActivity : AppCompatActivity() {

    open val debugTag: String by lazy {
        javaClass.simpleName
    }

    companion object {
        const val PARSE_TYPE_TEXT = 0
        const val PARSE_TYPE_ASSET = 1
        const val PARSE_TYPE_URL = 2
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (descriptionData() == null && descriptionData() == null) {
            return
        }
        val contentFrame = findViewById<FrameLayout>(android.R.id.content)
        contentFrame?.addView(
                DraggableActionButton(this).apply {
                    onFloatBtnClickListener = View.OnClickListener {
                        openDescription()
                    }
                    isClickable = false
                    isFocusable = false
                }, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }


    open fun descriptionData(): String? {
        return null
    }

    open fun descriptionType(): Int {
        return PARSE_TYPE_TEXT
    }

    open fun openDescription() {
        descriptionData()?.let {
            when (val type = descriptionType()) {
                PARSE_TYPE_URL -> WebDialog.newInstance(it)
                        .show(supportFragmentManager, "WebDialog")
                PARSE_TYPE_TEXT,
                PARSE_TYPE_ASSET -> ExplainDialog.newInstance(it, type)
                        .show(supportFragmentManager, "ExplainDialog")
            }
        }
    }
}


class DraggableActionButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewDragHelper: ViewDragHelper
    var onFloatBtnClickListener: OnClickListener? = null

    init {
        val floatActionButton = setFloatActionButton()
        viewDragHelper = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View?, pointerId: Int): Boolean {
                return true
            }

            override fun clampViewPositionVertical(child: View?, top: Int, dy: Int): Int {
                Log.d("DraggableActionButton", "top=$top; dy=$dy")
                return top
            }

            override fun clampViewPositionHorizontal(child: View?, left: Int, dx: Int): Int {
                Log.d("DraggableActionButton", "left=$left; dx=$dx")
                return left
            }

            override fun getViewVerticalDragRange(child: View?): Int {
                return if (floatActionButton === child) child.height else 0
            }

            override fun getViewHorizontalDragRange(child: View?): Int {
                return if (floatActionButton === child) child.width * 2 else 0
            }
        })
    }

    private fun setFloatActionButton(): FloatingActionButton {
        val lp = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        lp.gravity = Gravity.BOTTOM or Gravity.END
        val floatActionButton = FloatingActionButton(context).apply {
            setOnClickListener { onFloatBtnClickListener?.onClick(it) }
        }
        floatActionButton.id = R.id.action_settings
        dp2px(context, 5f).let { margin: Int ->
            lp.setMargins(margin, margin, margin, margin)
        }
        addView(floatActionButton, lp)
        return floatActionButton
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        viewDragHelper.processTouchEvent(event)
        return false
    }
}
