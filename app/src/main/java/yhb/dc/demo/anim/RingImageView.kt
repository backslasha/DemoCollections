package yhb.dc.demo.anim

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue

class RingImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : android.support.v7.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val ringDrawable: RingDrawable = RingDrawable(Color.RED, dp2px(context, 5f).toFloat())

    init {
        ValueAnimator.ofFloat(5f, 40f, 5f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                setRingThickness(it.animatedValue as Float)
            }
        }.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        ringDrawable.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ringDrawable.onBoundsChange(Rect(0, 0, w, h))
    }

    private fun setRingThickness(thickness: Float) {
        ringDrawable.setThickness(dp2px(context, thickness).toFloat())
        invalidate()
    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
    }
}


private class RingDrawable(color: Int, thickness: Float) : Drawable() {

    private val mPaint: Paint = Paint()
    private var mRadius = 0f

    override fun draw(canvas: Canvas) {
        if (mRadius <= 0) {
            return
        }
        canvas.drawCircle(mRadius, mRadius, mRadius - mPaint.strokeWidth / 2, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    public override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mRadius = bounds.width() / 2f
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setThickness(thickness: Float) {
        mPaint.strokeWidth = thickness
    }

    init {
        mPaint.color = color
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = thickness
    }
}