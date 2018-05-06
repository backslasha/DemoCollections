package yhb.dc.demo.tranistion;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import yhb.dc.R;


/**
 * Created by yhb on 18-4-5.
 */

public class MusicCoverView extends android.support.v7.widget.AppCompatImageView {

    public static final int SHAPE_RECTANGLE = 0;
    public static final int SHAPE_CIRCLE = 1;

    private final Path mClipPath = new Path();
    private final Path mRectPath = new Path();
    private float mRadius;
    private int mShape;

    public MusicCoverView(Context context) {
        super(context, null);
    }

    public MusicCoverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MusicCoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MusicCoverView);
        mRadius = array.getInteger(R.styleable.MusicCoverView_radius, -1);
        mShape = array.getInt(R.styleable.MusicCoverView_shape, SHAPE_RECTANGLE);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(mClipPath);
        super.onDraw(canvas);
        canvas.drawPath(mClipPath,new Paint());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h);
        resetRoundPaths(mRadius);
    }

    private void resetRoundPaths(float radius) {

        final int w = getWidth();
        final int h = getHeight();
        final float centerX = w / 2f;
        final float centerY = h / 2f;

        mClipPath.reset();
        mClipPath.addCircle(centerX, centerY, radius, Path.Direction.CW);
    }

}
