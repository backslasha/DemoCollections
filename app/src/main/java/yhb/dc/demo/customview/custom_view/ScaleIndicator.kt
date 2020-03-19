package yhb.dc.demo.customview.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import yhb.dc.R


class ScaleIndicator @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var maxValue: Float = 0f
    private var minValue: Float = 0f
    private var currentValue: Float = 0f
    private var intervalCount: Int = 0
    private val currentNodeRadius = dp2px(context, 8.0f).toFloat()
    private val nodeRadius = dp2px(context, 4.0f).toFloat()
    private val nodeColor = Color.LTGRAY
    private val lineColor = Color.LTGRAY
    private val textColor = Color.LTGRAY

    private val linePaint = Paint()
    private val nodePaint = Paint()
    private val textPaint = Paint()

    private var isTouching = false

    init {
        // update attrs

        // update paint
        linePaint.isDither = true
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = dp2px(context, 2f).toFloat()
        linePaint.style = Paint.Style.STROKE
        linePaint.pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 0f)

        textPaint.isDither = true
        textPaint.isAntiAlias = true
        textPaint.strokeWidth = dp2px(context, 2f).toFloat()
        textPaint.style = Paint.Style.STROKE
        textPaint.textSize = dp2px(context, 120f).toFloat()

        nodePaint.isDither = true
        nodePaint.isAntiAlias = true
        nodePaint.strokeWidth = dp2px(context, 2f).toFloat()
        nodePaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawLine(canvas)
            drawNode(canvas)
            drawText(canvas.apply {
                translate(width / 2 + currentNodeRadius, 0f)
            })
        }
    }

    private fun drawText(canvas: Canvas) {
        // draw current Text
        val cy = height - height * (currentValue / maxValue) + currentNodeRadius / 2
        val content = currentValue.toString()
        val rect = Rect()
        textPaint.getTextBounds(content, 0, content.length, rect)
        canvas.drawText(content, -rect.left.toFloat(), cy + ((rect.bottom - rect.top) / 2).toFloat(), textPaint)
    }

    private fun drawLine(canvas: Canvas) {
        val centerX = (canvas.width - linePaint.strokeWidth) / 2
        canvas.drawLine(centerX, 0f, centerX, height.toFloat(), linePaint)
    }

    private fun drawNode(canvas: Canvas) {
        val interval = (maxValue - minValue) / intervalCount
        val cx = (canvas.width - linePaint.strokeWidth) / 2
        var value = maxValue
        while (value >= minValue) {
            val cy = height - height * (value / maxValue) + nodeRadius / 2
            canvas.drawCircle(cx, cy, nodeRadius, nodePaint)
            value -= interval
        }
        // draw current node
        val cy = height - height * (currentValue / maxValue) + currentNodeRadius / 2
        canvas.drawCircle(cx, cy, currentNodeRadius, nodePaint)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            ACTION_DOWN -> {
                isTouching = true
                setCurrentValue(maxValue * ((height - event.y) / height))
                return true
            }
            ACTION_UP, ACTION_CANCEL -> {
                isTouching = false
                return true
            }
            ACTION_MOVE -> {
                setCurrentValue(maxValue * ((height - event.y) / height))
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setCurrentValue(value: Float) {
        val validValue = if (value < minValue) minValue else value
        this.currentValue = if (validValue > maxValue) maxValue else validValue
        invalidate()
    }

    companion object {
        fun debugSetup(indicator: ScaleIndicator) {
            indicator.apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                maxValue = 10f
                minValue = 0f
                intervalCount = 5
                currentValue = 3.7f
                setBackgroundColor(context.resources.getColor(R.color.pink_translucent))
            }
        }
    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
    }
}