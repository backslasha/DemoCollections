package yhb.chorus.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import yhb.chorus.R;

/**
 * Created by yhb on 18-4-5.
 */

public class MusicCoverView extends android.support.v7.widget.AppCompatImageView {

    private final Path mClipPath = new Path();
    private final Path mRectPath = new Path();
    private float mRadius;

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
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(mClipPath);
        super.onDraw(canvas);
    }

    private void resetPaths() {

        final int w = getWidth();
        final int h = getHeight();
        final float centerX = w / 2f;
        final float centerY = h / 2f;

        mClipPath.reset();
        mClipPath.addCircle(centerX, centerY, mRadius, Path.Direction.CW);
    }

}
