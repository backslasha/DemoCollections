package yhb.dc.demo.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

public class Ball extends android.support.v7.widget.AppCompatButton {
    private WindowManager mWindowManager;

    public Ball(Context context) {
        this(context, null);
    }

    public Ball(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ball(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
    }

    private float preX, preY;
    private boolean mMoved = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMoved = false;
                preX = event.getX();
                preY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float x = event.getX();
                float y = event.getY();

                float dx = x - preX;
                float dy = y - preY;

                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.x += (int) dx;
                    layoutParams.y += (int) dy;
                    mWindowManager.updateViewLayout(this, layoutParams);
                }
                mMoved = true;
                return true;
            case MotionEvent.ACTION_UP:
                if (mMoved) {
                    mMoved = false;
                    return true;
                }
                break;

        }
        return super.onTouchEvent(event);
    }
}
