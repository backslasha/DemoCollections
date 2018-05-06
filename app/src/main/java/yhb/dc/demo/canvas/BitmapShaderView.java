package yhb.dc.demo.canvas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import yhb.dc.R;

public class BrickView extends View {
    private Paint mFillPaint, mStrokePaint;// 填充和描边的画笔
    private BitmapShader mBitmapShader;// Bitmap着色器

    private float posX, posY;// 触摸点的XY坐标

    public BrickView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化画笔
        initPaint(context);
    }

    /**
     * 初始化画笔
     * @param context
     */
    private void initPaint(Object context) {
        /*
         * 实例化描边画笔并设置参数
         */
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaint.setColor(0xFF000000);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);

        // 实例化填充画笔
        mFillPaint = new Paint();


        // 获取屏幕尺寸数据
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // 获取屏幕中点坐标
        int screenX = displayMetrics.widthPixels / 2;
        int screenY = displayMetrics.heightPixels / 2;

        /*
         * 生成BitmapShader
         */
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.REPEAT);


        Matrix matrix = new Matrix();
        matrix.setTranslate(screenX, screenY);
        mBitmapShader.setLocalMatrix(matrix);


        mFillPaint.setShader(mBitmapShader);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * 手指移动时获取触摸点坐标并刷新视图
         */
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            posX = event.getX();
            posY = event.getY();

            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 设置画笔背景色
        canvas.drawColor(Color.DKGRAY);

        /*
         * 绘制圆和描边
         */
        canvas.drawCircle(posX, posY, 300, mFillPaint);
        canvas.drawCircle(posX, posY, 300, mStrokePaint);
    }
}
