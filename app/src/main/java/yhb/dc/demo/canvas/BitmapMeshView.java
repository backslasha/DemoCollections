package yhb.dc.demo.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import yhb.dc.R;

/**
 * Created by yhb on 18-4-5.
 */

public class BitmapMeshView extends View {
    private static final int xCount = 12, yCount = 12;// 分割数
    private static final int COUNT = (xCount + 1) * (yCount + 1);// 交点数

    private Bitmap mBitmap;// 位图对象

    private float[] matrixOriganal = new float[COUNT * 2];// 基准点坐标数组
    private float[] matrixMoved = new float[COUNT * 2];// 变换后点坐标数组

    private float clickX, clickY;// 触摸屏幕时手指的xy坐标

    private Paint origPaint, movePaint, linePaint;// 基准点、变换点和线段的绘制Paint

    public BitmapMeshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 实例画笔并设置颜色
        origPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        origPaint.setColor(0x660000FF);
        movePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        movePaint.setColor(0x99FF0000);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0xFFFFFB00);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 获取位图资源
        mBitmap = CanvasActivity.loadBitmap(getContext(), R.drawable.girl, w, h);

        // 初始化坐标数组
        int index = 0;
        for (int i = 0; i <= yCount; i++) {
            float fy = mBitmap.getHeight() * i / yCount;
            for (int j = 0; j <= xCount; j++) {
                float fx = mBitmap.getWidth() * j / xCount;
                setXY(matrixMoved, index, fx, fy);
                setXY(matrixOriganal, index, fx, fy);
                index += 1;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void setXY(float[] array, int index, float x, float y) {
        array[index * 2] = x;
        array[index * 2 + 1] = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制网格位图
        canvas.drawBitmapMesh(mBitmap, xCount, yCount, matrixMoved, 0, null, 0, null);

        // 绘制参考元素
        drawGuide(canvas);
    }

    private void drawGuide(Canvas canvas) {
        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = matrixOriganal[i];
            float y = matrixOriganal[i + 1];
            canvas.drawCircle(x, y, 4, origPaint);

            float x1 = matrixOriganal[i];
            float y1 = matrixOriganal[i + 1];
            float x2 = matrixMoved[i];
            float y2 = matrixMoved[i + 1];
            canvas.drawLine(x1, y1, x2, y2, origPaint);
        }

        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = matrixMoved[i];
            float y = matrixMoved[i + 1];
            canvas.drawCircle(x, y, 4, movePaint);
        }

        canvas.drawCircle(clickX, clickY, 6, linePaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clickX = event.getX();
        clickY = event.getY();
        smudge();
        invalidate();
        return true;
    }

    private void smudge() {
        for (int i = 0; i < COUNT * 2; i += 2) {

            float xOriginal = matrixOriganal[i];
            float yOriginal = matrixOriganal[i + 1];

            float deltaX = clickX - xOriginal;
            float deltaY = clickY - yOriginal;

            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            float morph;
            if (distance >= 100) {
                morph = 0;
            } else {
                morph = distance;
            }
            if (xOriginal < clickX) {
                matrixMoved[i] = xOriginal - morph;
            } else {
                matrixMoved[i] = xOriginal + morph;
            }

            if (yOriginal < clickY) {
                matrixMoved[i + 1] = yOriginal - morph;
            } else {
                matrixMoved[i + 1] = yOriginal + morph;
            }
        }
    }
}
