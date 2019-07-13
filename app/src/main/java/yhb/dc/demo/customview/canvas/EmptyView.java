package yhb.dc.demo.customview.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Color.WHITE;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by yhb on 18-4-10.
 */

public class EmptyView extends View {


    private Paint mPaint;

    public EmptyView(Context context) {
        this(context, null);
        mPaint = new Paint();
        restorePaint(mPaint);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case ACTION_DOWN:
                break;
            case ACTION_MOVE:
                break;
            case ACTION_UP:
        }

        return true;
    }



    private void restorePaint(Paint paint) {
        paint.setColor(WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        paint.setTextSize(42);

    }

}
