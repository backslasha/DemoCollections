package yhb.dc.demo.demo_view.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Color.RED;

public class ClipRectView extends View {
    private final Paint mPaint, mPaint0;
    private final Path mPath;

    public ClipRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint0 = new Paint();
        restorePaint(mPaint);
        mPath = new Path();
    }

    public static final Region.Op[] ops = Region.Op.values();

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFF29B76);

        int regionSize = getMeasuredWidth() / 2;
        int top = 100, left = 100, height = 200, width = 200, offset = 100;
        for (int i = 0; i < ops.length; i++) {
            canvas.save();
            canvas.clipRect(left, top, left + width, top + height);
            canvas.clipRect(left + offset, top + offset, left + width + offset, top + height + offset, ops[i]);
            canvas.drawColor(RED);
            canvas.restore();

            // 绘制框框帮助我们观察
            canvas.drawRect(left, top, left + width, top + height, mPaint0);
            canvas.drawRect(left + offset, top + offset, left + width + offset, top + height + offset, mPaint0);
            canvas.drawText(String.valueOf(ops[i]), left + offset * 2, top + height + offset * 2, mPaint0);

            // 平移画布重新画一个
            if (i % 2 == 0) {
                canvas.translate(regionSize, 0);
            } else {
                canvas.translate(-regionSize, 0);
            }

            if ((i + 1) % 2 == 0) {
                canvas.translate(0, regionSize);
            }

        }
    }

    private void restorePaint(Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(12);
        paint.setAntiAlias(true);
        paint.setDither(true);

        mPaint0.set(mPaint);
        mPaint0.setColor(Color.WHITE);
        mPaint0.setTextSize(72);
        mPaint0.setStrokeWidth(6);
        mPaint0.setStyle(Paint.Style.STROKE);
    }

}