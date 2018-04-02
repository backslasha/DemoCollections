package yhb.dc.demo.custom_view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import yhb.dc.R;

import static android.content.ContentValues.TAG;


public class JournalView extends ViewGroup {


    private static final int DEFAULT_WIDTH = 36;
    private static final int DEFAULT_FRAME_HEIGHT = 36;
    private static final int DEFAULT_FRAME_COLOR = Color.BLACK;
    private static final int DEFAULT_PAPER_HEIGHT = 54;
    private static final int DEFAULT_PAPER_COLOR = Color.WHITE;

    private int mFrameColor = DEFAULT_FRAME_COLOR;
    private int mFrameHeight = DEFAULT_FRAME_HEIGHT;
    private float mDefaultPaperHeight = DEFAULT_PAPER_HEIGHT;
    private RectF mRectFLeft = null, mRectFRight = null;


    private Paint mFramePaint, mPaperPaint;
    private Path mPath = new Path();

    private Scroller mScroller;

    public JournalView(Context context) {
        this(context, null);
    }

    public JournalView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JournalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JournalView);
        mFrameColor = typedArray.getColor(R.styleable.JournalView_frame_color, DEFAULT_FRAME_COLOR);
        mFrameHeight = typedArray.getDimensionPixelSize(R.styleable.JournalView_frame_height, DEFAULT_FRAME_HEIGHT);
        mDefaultPaperHeight = typedArray.getDimensionPixelSize(R.styleable.JournalView_default_paper_height, DEFAULT_PAPER_HEIGHT);
        typedArray.recycle();

        mFramePaint = new Paint();
        mFramePaint.setDither(true);
        mFramePaint.setAntiAlias(true);
        mFramePaint.setStyle(Paint.Style.FILL);
        mFramePaint.setStrokeCap(Paint.Cap.ROUND);
        mFramePaint.setColor(mFrameColor);

        mPaperPaint = new Paint();
        mPaperPaint.setDither(true);
        mPaperPaint.setAntiAlias(true);
        mPaperPaint.setStyle(Paint.Style.FILL);
        mPaperPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaperPaint.setColor(DEFAULT_PAPER_COLOR);

        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            int wants = (int) Math.max(mDefaultPaperHeight + mFrameHeight / 2, mFrameHeight);
            result = (int) Math.min(wants, size);
        } else {
            result = 0;
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.min(DEFAULT_WIDTH, size);
        } else {
            result = 0;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRectFLeft = new RectF(0, 0, h, h);
        mRectFRight = new RectF(w - h, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFrame(canvas);
        drawPaper(canvas);
    }

    private void drawFrame(Canvas canvas) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        mRectFLeft.left = paddingLeft;
        mRectFLeft.top = paddingTop;
        mRectFLeft.right = mFrameHeight - paddingRight;
        mRectFLeft.bottom = mFrameHeight - paddingBottom;

        mRectFRight.left = getWidth() - mFrameHeight + paddingLeft;
        mRectFRight.top = paddingTop;
        mRectFRight.right = getWidth() - paddingRight;
        mRectFRight.bottom = mFrameHeight - paddingBottom;

        canvas.drawArc(mRectFLeft, -90f, -180f, false, mFramePaint);
        canvas.drawArc(mRectFRight, -90f, 180f, false, mFramePaint);
        canvas.drawRect(mRectFLeft.right - mRectFLeft.width() / 2, mRectFLeft.top, mRectFRight.left + mRectFRight.width() / 2, mRectFRight.bottom, mFramePaint);

    }

    private void drawPaper(Canvas canvas) {

        final int paperTop = (int) mRectFLeft.centerY();
        final int paperLeft = (int) mRectFLeft.centerX();
        final int paperRight = (int) mRectFRight.centerX();
        final int paperBottom = (int) (mRectFRight.centerY() + mDefaultPaperHeight - 20);
        canvas.drawRect(paperLeft, paperTop, paperRight, paperBottom, mPaperPaint);

        mPath.reset();
        mPath.moveTo(paperLeft, paperBottom);

        final int aliasCount = 40;
        final int aliasHeight = 20;
        int slop = (paperRight - paperLeft) / aliasCount;


        int x = paperLeft;
        int y;
        while (x + slop < paperRight) {

            x = x + slop;
            y = paperBottom + aliasHeight;

            mPath.lineTo(x, y);

            if (x < paperRight) {
                x = x + slop;
                y = paperBottom;

                mPath.lineTo(x, y);
            }
        }

        canvas.drawPath(mPath, mPaperPaint);
    }


    private boolean isDragging = false;
    private int lastY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getY();
                isDragging = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    int newY = (int) event.getY();
                    int delta = (int) (newY - lastY);

                    mDefaultPaperHeight += delta;
                    invalidate();

                    lastY = newY;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll(0, lastY, 0, lastY / 3);
                invalidate();
                isDragging = false;
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            mDefaultPaperHeight = mScroller.getCurrY();
            invalidate();
        }
    }
}
