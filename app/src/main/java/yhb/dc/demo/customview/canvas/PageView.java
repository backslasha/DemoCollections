package yhb.dc.demo.customview.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import static android.graphics.Color.WHITE;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yhb on 18-4-10.
 */

public class PageView extends View {

    private int mViewWidth;

    public Bitmap[] getBitmaps() {
        return mBitmaps;
    }

    public void setBitmaps(Bitmap[] bitmaps) {
        mBitmaps = bitmaps;
        if (mBitmaps.length != 0) {
            mCurrentPage = 0;
        }
    }

    private Bitmap[] mBitmaps;
    private Paint mPaint;

    public PageView(Context context) {
        this(context, null);
        mPaint = new Paint();
        restorePaint(mPaint);
    }

    public PageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmaps == null) {
            return;
        }

        for (int i = mBitmaps.length - 1; i >= 0; i--) {
            if (i < mCurrentPage) {
                break;
            }

            if (i == mCurrentPage) {
                if (lastPage) {
                    canvas.drawBitmap(mBitmaps[i], 0, 0, null);
                    break;
                }
                canvas.save();
                canvas.clipRect(0, 0, mPageX, getHeight());
                canvas.drawBitmap(mBitmaps[i], 0, 0, null);
                canvas.restore();
                continue;
            }
            canvas.drawBitmap(mBitmaps[i], 0, 0, null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mPageX == -1) {
            mPageX = w;
        }

        for (int i = 0; i < mBitmaps.length; i++) {
            mBitmaps[i] = Bitmap.createScaledBitmap(mBitmaps[i], w, h, true);
        }

        mViewWidth = w;

        leftBorder = mViewWidth * 1 / 5F;
        rightBorder = mViewWidth * 4 / 5F;
    }

    private float leftBorder, rightBorder;
    private int mPageX = -1;
    private int mCurrentPage = -1;
    private boolean firstPage = true;
    private boolean lastPage = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        firstPage = mCurrentPage == 0;
        lastPage = mCurrentPage == mBitmaps.length - 1;

        switch (event.getAction()) {
            case ACTION_DOWN:
                if (event.getX() < getMeasuredWidth() / 2) {
                    if (!firstPage) {
                        mCurrentPage--;
                    } else {
                        Toast.makeText(getContext(), "this is the first page.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    if (lastPage) {
                        Toast.makeText(getContext(), "this is last page.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                break;
            case ACTION_MOVE:
                mPageX = (int) event.getX();
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
                    mPageX -= 16;
                    invalidate();
                    if (mPageX > 0) {
                        judgeSlideAuto();
                    } else {
                        if (!lastPage) {
                            mCurrentPage++;
                        }
                        mPageX = mViewWidth;
                    }
                }
            }, 16);

        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPageX += 16;
                    invalidate();
                    if (mPageX < mViewWidth) {
                        judgeSlideAuto();
                    } else {
                        mPageX = mViewWidth;
                    }
                }
            }, 16);
        }
    }

    private boolean stopAtLeft() {
        return mPageX < getMeasuredWidth() / 2;
    }

    private void restorePaint(Paint paint) {
        paint.setColor(WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        paint.setTextSize(42);

    }

}
