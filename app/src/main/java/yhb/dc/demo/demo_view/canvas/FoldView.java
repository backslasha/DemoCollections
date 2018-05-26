package yhb.dc.demo.demo_view.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yhb on 18-4-10.
 */

public class FoldView extends View {


    private Paint mPaint;
    private Path mPath;

    private int mW;
    private int mH;
    private int mY, mX;
    private Region mRegion;

    public FoldView(Context context) {
        this(context, null);
    }

    public FoldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPath = new Path();
        mRegion = new Region();
        restorePaint(mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(WHITE);
        canvas.drawCircle(mX, mY, 15, mPaint);
        if (mW == mX) {
            mX = mW - 1;
        }
        if (mY == mH) {
            mY = mH - 1;
        }
        int x = ((mH - mY) * (mH - mY) + (mW - mX) * (mW - mX)) / (2 * (mW - mX));
        int y = ((mW - mX) * (mW - mX) + (mY - mH) * (mY - mH)) / (-2 * (mY - mH));
        mPath.reset();
        mPath.moveTo(mX, mY);
        mPath.lineTo(mW - x, mH);

        if (y > mH) {
            int k0 = -(y - mH) - mY / (mW - mX);
            int b0 = mY - k0 * mX;
            int x0 = -b0 / k0;
            int y0 = 0;
            int k1 = mH - -(y - mH) / (mW - x - mW);
            int b1 = -(y - mH) - k1 * mW;
            int x1 = -b1 / k1;
            int y1 = 0;
            mPath.lineTo(x1, y1);
            mPath.lineTo(x0, y0);
        } else {
            mPath.lineTo(mW, mH - y);
        }
        mPath.close();
        canvas.drawPath(mPath, mPaint);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = w;
        mH = h;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mX = (int) event.getX();
        mY = (int) event.getY();
        switch (event.getAction()) {
            case ACTION_DOWN:
            case ACTION_MOVE:
                if ((mH - mY) * (mH - mY) + mX * mX > mW * mW) {
                    // 如果不在则通过x坐标强行重算y坐标
                    mY = (int) (-Math.sqrt(mW * mW - mX * mX) + mH);
                }
                if (mY >= mH - 30) {
                    mY = mH - 30;
                }
                invalidate();
                break;
            case ACTION_UP:
                judgeSlideAuto();
        }

        return true;
    }


    /**
     * 判断是否需要自动滑动
     * 根据参数的当前值判断绘制
     */
    private void judgeSlideAuto() {
        if (stopAtLeft()) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mX -= 16;
                    mY += 16;
                    invalidate();
                    if (mX > -mW / 2 || mY < mH) {
                        judgeSlideAuto();
                    }
                }
            }, 16);

        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mX += 16;
                    float i = mW - mX;
                    float j = mH - mY;
                    mY += (16f / i * j);
                    invalidate();
                    if (mX < mW || mY < mH) {
                        judgeSlideAuto();
                    }
                }
            }, 16);
        }
    }

    private boolean stopAtLeft() {
        return mX < getMeasuredWidth() / 2;
    }


    private void restorePaint(Paint paint) {
        paint.setColor(BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        paint.setTextSize(42);

    }

}
