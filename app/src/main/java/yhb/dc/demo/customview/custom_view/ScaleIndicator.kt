package yhb.dc.demo.customview.custom_view

import android.animation.Animator
import android.animation.ValueAnimator
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
import android.view.animation.DecelerateInterpolator
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
    private var animator: Animator? = null

    private val textRect = Rect()
    private val centerYs = ArrayList<Float>()
    private val contents = ArrayList<String>()

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

            centerYs.clear()
            textRect.set(0, 0, 0, 0)
            contents.clear()

            var value = maxValue
            val interval = (maxValue - minValue) / intervalCount

            for (i in 0..intervalCount) {
                centerYs.add(height - height * (value / maxValue) + nodeRadius / 2)
                contents.add(content(value))
                value -= interval
            }

            drawLine(canvas, width / 2 - linePaint.strokeWidth / 2)
            drawNode(canvas, width / 2 - linePaint.strokeWidth / 2, centerYs)
            drawText(canvas, width / 2 - currentNodeRadius, centerYs, contents)
        }
    }

    private fun drawText(canvas: Canvas, cx: Float, cys: List<Float>, contents: List<String>) {
        for (index in cys.indices) {
            textPaint.textSize = dp2px(context, 26f).toFloat()
            textPaint.getTextBounds(contents[index], 0, contents[index].length, textRect)
            canvas.drawText(contents[index], cx - textRect.right - textRect.left - textRect.left,
                    cys[index] + ((textRect.bottom - textRect.top) / 2).toFloat(), textPaint)
        }
        // draw current Text
        canvas.translate(width / 2 + currentNodeRadius, 0f)
        val content = content(currentValue)
        val cy = height - height * (currentValue / maxValue) + currentNodeRadius / 2
        textPaint.textSize = dp2px(context, 30f).toFloat()
        textPaint.getTextBounds(content, 0, content.length, textRect)
        canvas.drawText(content, -textRect.left.toFloat(), cy + ((textRect.bottom - textRect.top) / 2).toFloat(), textPaint)
    }

    private fun drawLine(canvas: Canvas, cx: Float) {
        canvas.drawLine(cx, 0f, cx, height.toFloat(), linePaint)
    }

    private fun drawNode(canvas: Canvas, cx: Float, cys: List<Float>) {
        for (cy in cys) {
            canvas.drawCircle(cx, cy, nodeRadius, nodePaint)
        }
        // draw current node
        val cy = height - height * (currentValue / maxValue) + currentNodeRadius / 2
        canvas.drawCircle(cx, cy, currentNodeRadius, nodePaint)
    }

    private fun content(float: Float): String {
        val floatStr = float.toString()
        val index = floatStr.indexOf('.')
        return "${floatStr.subSequence(0, (index + 3).coerceAtMost(floatStr.length))}x"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            ACTION_DOWN -> {
                isTouching = true
                setCurrentValue(maxValue * ((height - event.y) / height), false)
                return true
            }
            ACTION_UP, ACTION_CANCEL -> {
                isTouching = false
                return true
            }
            ACTION_MOVE -> {
                setCurrentValue(maxValue * ((height - event.y) / height), false)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setCurrentValue(value: Float, smooth: Boolean) {
        val validValue = if (value < minValue) minValue else value
        if (smooth) {
            smoothChangeTo(if (validValue > maxValue) maxValue else validValue)
        } else {
            currentValue = if (validValue > maxValue) maxValue else validValue
            invalidate()
        }
    }

    private fun smoothChangeTo(dest: Float) {
        val oldAnim = animator
        if (oldAnim != null && oldAnim.isRunning) {
            oldAnim.cancel()
        }
        animator = ValueAnimator.ofFloat(currentValue, dest).apply {
            addUpdateListener {
                currentValue = it.animatedValue as Float
                invalidate()
            }
            duration = 100
            interpolator = DecelerateInterpolator()
            start()
        }
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