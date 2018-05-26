package yhb.dc.demo.demo_view.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class QuadToView extends View {
    private Path mPath;// 路径对象
    private Paint mPaint;// 画笔对象
    private int mWidth, mHeight;// 控件宽高
    private float mCtrX, mCtrY;// 控制点的xy坐标
    private boolean isLeaking = true, toRight = true;// 判断控制点是该右移还是左移

    public QuadToView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 实例化画笔并设置参数
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0xFFA2D6AE);

        // 实例化路径对象
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 获取控件宽高
        mWidth = w;
        mHeight = h;

        // 计算端点Y坐标
        mCtrX = 0;
        mCtrY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCtrX >= mWidth) {
            toRight = false;
        } else if (mCtrX < 0) {
            toRight = true;
        }

        if (!toRight) {
            mCtrX -= 32;
        } else {
            mCtrX += 32;
        }

        if (mCtrY >= mHeight) {
            isLeaking = false;
        } else if (mCtrY <= 0) {
            isLeaking = true;
        }

        if (isLeaking) {
            mCtrY += 4;
        } else {
            mCtrY -= 4;
        }

        final int startX = -(int) (1 / 8F * mWidth);
        final int startY = (int) (mCtrY + (int) (1 / 8F * mHeight));
        final int stopX = mWidth + (int) (1 / 8F * mWidth);
        final int stopY = startY;

        mPath.reset();
        mPath.moveTo(startX, startY);
        mPath.quadTo(mCtrX, mCtrY, stopX, stopY);
        mPath.lineTo(stopX, mHeight);
        mPath.lineTo(startX, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 16);
    }
}