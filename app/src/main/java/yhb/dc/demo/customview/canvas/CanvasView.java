package yhb.dc.demo.customview.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Color.WHITE;

/**
 * Created by yhb on 18-4-5.
 */

public class CanvasView extends View {
    private static final float RADIUS = 130;
    private static final int SPACE = 160;
    private Paint mPaint;// 画笔对象

    private int mViewWidth, mViewHeight;// 控件宽高

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(WHITE);
        mPaint.setTextSize(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(0xFFF29B76);

        final int cx = canvas.getWidth() / 2;
        final int cy = canvas.getHeight() / 2;


        canvas.save();
        canvas.translate(cx, cy);
        drawCircle(canvas);
        drawText(canvas, "BALABALA");
        canvas.restore();

        canvas.save();
        canvas.rotate(-30, cx, cy);
        canvas.translate(cx, cy - (RADIUS * 2 + SPACE));
        drawCircle(canvas);
        drawText(canvas, "BALABALA");
        drawLine(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(-30, cx, cy);
        canvas.translate(cx, cy - (RADIUS * 2 + SPACE) * 2);
        drawCircle(canvas);
        drawText(canvas, "BALABALA");
        drawLine(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(40, cx, cy);
        canvas.translate(cx, cy - (RADIUS * 2 + SPACE));
        drawCircle(canvas);

        canvas.rotate(-40);
        drawText(canvas, "BALABALA");
        canvas.rotate(40);

        drawLine(canvas);
        drawArc(canvas);
        canvas.restore();


        canvas.save();
        canvas.rotate(110, cx, cy);
        canvas.translate(cx, cy - (RADIUS * 2 + SPACE));
        drawCircle(canvas);
        drawText(canvas, "BALABALA");
        drawLine(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(180, cx, cy);
        canvas.translate(cx, cy - (RADIUS * 2 + SPACE));
        drawCircle(canvas);
        drawText(canvas, "BALABALA");
        drawLine(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(250, cx, cy);
        canvas.translate(cx, cy - (RADIUS * 2 + SPACE));
        drawCircle(canvas);
        drawText(canvas, "BALABALA");
        drawLine(canvas);
        canvas.restore();

    }


    private void drawArc(Canvas canvas) {
        mPaint.setColor(0x55EC6941);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.save();
        canvas.rotate(-202);
        float radius = RADIUS + 30;
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(rectF, 0, 135f, true, mPaint);
        mPaint.setColor(WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, 0, 135f, false, mPaint);

        canvas.rotate(90);
        canvas.translate(0,-(int) (radius + 50));
        for (int i = 0; i < 5; i++) {
            canvas.drawText("balala", -(int) mPaint.measureText("balala") / 2, 0, mPaint);
            canvas.rotate(33.75f,0,(int) (radius + 50));
        }
        canvas.restore();

    }

    private void drawText(Canvas canvas, String content) {
        float textWidth = mPaint.measureText(content);
        float padding = RADIUS - textWidth / 2;
        canvas.drawText(content, -RADIUS + padding, mPaint.getFontMetricsInt().leading + mPaint.getFontMetricsInt().descent, mPaint);
    }

    private void drawLine(Canvas canvas) {
        canvas.drawLine(0, RADIUS, 0, RADIUS + SPACE, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(0, 0, RADIUS, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        /*
         * 获取控件宽高
         */
        mViewWidth = w;
        mViewHeight = h;
    }
}
