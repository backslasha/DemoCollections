package yhb.dc.demo.demo_view.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.WHITE;

public class PathView extends View {
    private static final int MAX_ITEM_COUNT = 10;
    private static final int PADDING = 32; // 最小间隔单位
    private static final float QUARTER_LENGTH = 16; // 区间标记长度
    private static final int ARROW_LENGTH = 36;// 坐标轴末尾多余出来的一段长度

    private List<PointF> mPoints;
    private String mTitleX, mTitleY;
    private float mDataUnitX, mDataUnitY; // 一格子数据的大小
    private float mUnitY, mUnitX; // 一格子的长度 in pixel
    private float mXAxisWidth, mYAxisHeight; // x、y 轴的长度 in pixel，不包括 ARROW_LENGTH
    private float mOriginX, mOriginY; // 原点的坐标 in pixel
    private Paint mPaint;
    private Path mPath;
    private int mSegmentCount; // x、y 奏分割成的段数，将等于数据点 Point 的个数


    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        restorePaint(mPaint);

        mPath = new Path();

        List<PointF> data = new ArrayList<>();
        data.add(new PointF(0.0f, 7));
        data.add(new PointF(1.5f, 12));
        data.add(new PointF(2.1f, 15));
        data.add(new PointF(3.0f, 9));
        data.add(new PointF(4.8f, 22));
        data.add(new PointF(5.2f, 19));
        data.add(new PointF(6.3f, 4));
        setData(data, "x", "y");
    }

    public synchronized void setData(List<PointF> pointFs, String titleX, String titleY) {
        if (null == pointFs || pointFs.size() == 0)
            throw new IllegalArgumentException("No data to display !");

        if (pointFs.size() > MAX_ITEM_COUNT)
            throw new IllegalArgumentException("The data is too long to display !");

        this.mPoints = pointFs;
        this.mTitleX = titleX;
        this.mTitleY = titleY;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private float findMinX(List<PointF> points) {
        return 0;
    }

    private float findMaxX(List<PointF> points) {
        int max = Integer.MIN_VALUE;
        for (PointF point : points) {
            if (max < point.x) {
                max = (int) point.x;
            }
        }
        return max;
    }

    private float findMinY(List<PointF> points) {
        return 0;
    }

    private float findMaxY(List<PointF> points) {
        int max = Integer.MIN_VALUE;
        for (PointF point : points) {
            if (max < point.y) {
                max = (int) point.y;
            }
        }
        return max;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mPoints == null || mPoints.size() == 0) {
            return;
        }

        mSegmentCount = mPoints.size();

        mOriginX = PADDING * 2 + mPaint.measureText(String.format(Locale.CHINA, "%.2f", mDataUnitX * findMaxX(mPoints)));
        mOriginY = h - PADDING * 2 - (-mPaint.getFontMetricsInt().top + mPaint.getFontMetricsInt().bottom);

        mXAxisWidth = w - PADDING * 2 - mOriginX - ARROW_LENGTH;
        mYAxisHeight = mOriginY - PADDING * 2 - ARROW_LENGTH;

        final float maxX = findMaxX(mPoints);
        final float minX = findMinX(mPoints);
        final float maxY = findMaxY(mPoints);
        final float minY = findMinY(mPoints);

        mDataUnitX = (float) Math.ceil((maxX - minX) / mSegmentCount);
        mDataUnitY = (float) Math.ceil((maxY - minY) / mSegmentCount);

        mUnitX = mXAxisWidth / mSegmentCount;
        mUnitY = mYAxisHeight / mSegmentCount;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFF29B76);
        if (mPoints == null || mPoints.size() == 0) {
            return;
        }
        drawCoordinate(canvas);
        drawGrid(canvas);
        drawPoints(canvas);

    }

    // 画数据点，并连线
    private void drawPoints(Canvas canvas) {
        mPath.reset();

        final float dataMaxX = mDataUnitX * mSegmentCount;
        final float dataMaxY = mDataUnitY * mSegmentCount;
        float x, y;
        for (PointF point : mPoints) {
            x = mOriginX + point.x / dataMaxX * mXAxisWidth;
            y = mOriginY - point.y / dataMaxY * mYAxisHeight;
            mPath.addCircle(x, y, 4, Path.Direction.CW);
        }

        float x1, y1;
        for (int i = 1; i < mPoints.size(); i++) {
            PointF last = mPoints.get(i - 1);
            PointF cur = mPoints.get(i);
            x = mOriginX + cur.x / dataMaxX * mXAxisWidth;
            y = mOriginY - cur.y / dataMaxY * mYAxisHeight;
            x1 = mOriginX + last.x / dataMaxX * mXAxisWidth;
            y1 = mOriginY - last.y / dataMaxY * mYAxisHeight;
            mPath.moveTo(x1, y1);
//            mPath.quadTo(x1 / 2 + x / 2, y, x, y);
            mPath.lineTo(x, y);
        }

        canvas.drawPath(mPath, mPaint);
    }

    // 画网格
    private void drawGrid(Canvas canvas) {
        mPaint.setStrokeWidth(6);
        mPath.reset();
        mPath.moveTo(mOriginX, mOriginY);

        for (int i = 1; i <= mSegmentCount; i++) {
            mPath.moveTo(mOriginX, mOriginY - i * mUnitY);
            mPath.lineTo(mOriginX + mXAxisWidth, mOriginY - i * mUnitY);
        }
        mPath.moveTo(mOriginX, mOriginY);
        for (int i = 1; i <= mSegmentCount; i++) {
            mPath.moveTo(mOriginX + i * mUnitX, mOriginY);
            mPath.lineTo(mOriginX + i * mUnitX, mOriginY - mYAxisHeight);
        }
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(0x55EC6941);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new RectF(mOriginX, mOriginY - mYAxisHeight, mOriginX + mXAxisWidth, mOriginY), mPaint);

        restorePaint(mPaint);

    }


    // 画坐标，包括 x、y 轴和区间标记
    private void drawCoordinate(Canvas canvas) {
        final float width = getMeasuredWidth();

        // x axis
        float endX = width - 2 * PADDING;
        canvas.drawLine(mOriginX, mOriginY, endX, mOriginY, mPaint);

        // y axis
        float endY = 2 * PADDING;
        canvas.drawLine(mOriginX, mOriginY, mOriginX, endY, mPaint);


        // x axis quarters
        for (int i = 0; i <= mSegmentCount; i++) {
            canvas.drawLine(mOriginX + mUnitX * i, mOriginY, mOriginX + mUnitX * i, mOriginY - QUARTER_LENGTH, mPaint);

            // draw text below
            mPaint.setStrokeWidth(4);
            String str = String.format(Locale.CHINA, "%.2f", mDataUnitX * i);
            float textX = (mOriginX + mUnitX * i) - mPaint.measureText(str) / 2;
            float textY = mOriginY - mPaint.getFontMetricsInt().top + mPaint.getFontMetricsInt().bottom;
            canvas.drawText(str, textX, textY, mPaint);
            mPaint.setStrokeWidth(12);
        }

        // y axis quarters
        for (int i = 1; i <= mSegmentCount; i++) {
            canvas.drawLine(mOriginX, mOriginY - mUnitY * i, mOriginX + QUARTER_LENGTH, mOriginY - mUnitY * i, mPaint);

            // draw text left
            mPaint.setStrokeWidth(4);
            String str = String.format(Locale.CHINA, "%.2f", mDataUnitY * i);
            float textX = mOriginX - mPaint.measureText(str) - PADDING;
            float textY = mOriginY - (mUnitY * i) + mPaint.getFontMetricsInt().bottom;
            canvas.drawText(str, textX, textY, mPaint);
            mPaint.setStrokeWidth(12);

        }

    }

    private void restorePaint(Paint paint) {
        paint.setColor(WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        paint.setTextSize(42);

    }
}