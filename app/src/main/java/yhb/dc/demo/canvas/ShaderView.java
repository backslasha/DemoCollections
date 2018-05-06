package yhb.dc.demo.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yhb on 18-4-5.
 */

public class CanvasView extends View {
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
    }

    @Override
    protected void onDraw(Canvas canvas) {


        mPaint.setColor(Color.RED);
        canvas.drawRect(
                mViewWidth / 2 - 200,
                mViewHeight / 2 - 200,
                mViewWidth / 2 + 200,
                mViewHeight / 2 + 200,
                mPaint
        );

        canvas.save();
        canvas.rotate(30);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(
                mViewWidth / 2 - 100,
                mViewHeight / 2 - 100,
                mViewWidth / 2 + 100,
                mViewHeight / 2 + 100,
                mPaint
        );

        canvas.restore();

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(
                mViewWidth / 2 - 50,
                mViewHeight / 2 - 50,
                mViewWidth / 2 + 50,
                mViewHeight / 2 + 50,
                mPaint
        );
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
