package yhb.dc.demo.demo_view.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BubbleView extends View {

    private List<Circle> allCircles;
    private Paint mPaint;

    public BubbleView(Context context) {
        this(context, null);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int circleCount = 30;
        int circleMin = dp(80);
        int circleMax = dp(150);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        allCircles = createRandomCircles(circleCount, circleMin, circleMax);
        flatCircles();
    }

    private List<Circle> createRandomCircles(int circleCount, int circleMin, int circleMax) {
        List<Circle> result = new ArrayList<>();
        for (int i = 0; i < circleCount; i++) {
            Circle circle = new Circle(
                    (float) (Math.random() * getWidth()),
                    (float) (Math.random() * getHeight()),
                    (float) (circleMin + Math.random() * (circleMax - circleMin))
            );
            result.add(circle);
        }
        return result;
    }

    public void flatCircles() {

        final PointF center = new PointF(getWidth() / 2, getHeight() / 2);

        // orderby c.DistanceToCenter descending
        Collections.sort(allCircles, new Comparator<Circle>() {
            @Override
            public int compare(Circle c1, Circle c2) {
                return (int) (c1.d2Point(center) - c2.d2Point(center));
            }
        });

        List<Circle> sortedCircles = allCircles;

        // Cycle through circles for collision detection
        for (Circle c1 : sortedCircles) {
            for (Circle c2 : sortedCircles) {
                if (c1 != c2) {
                    float dx = c2.x - c1.x;
                    float dy = c2.y - c1.y;
                    float r = c1.radius + c2.radius;
                    float d = (dx * dx) + (dy * dy);
                    if (d < (r * r) - 0.01) {// - 0.01 why?
                        double sqrt_d = Math.sqrt(d);
                        float x = (float) (r * dx / sqrt_d - dx) * 0.5f;
                        float y = (float) (r * dy / sqrt_d - dy) * 0.5f;
                        c2.y += y;
                        c2.x += x;
                        c1.x -= x;
                        c1.y -= y;
                    }
                }
            }
        }

        // Contract all circles into the center ???
        float dampening = 0.1f / (float) allCircles.size();
        for (Circle c : sortedCircles) {
            float x = c.x - (float) (getWidth() / 2);
            float y = c.y - (float) (getHeight() / 2);
            x *= dampening;
            y *= dampening;
            c.x -= x;
            c.y -= y;
        }
    }


    public void drawCircles(Canvas canvas) {
        for (int i = 0; i < allCircles.size(); i++) {
            Circle c = allCircles.get(i);
            canvas.drawCircle(c.x, c.y, c.radius, mPaint);

//            if (i < HostCanvas.Children.Count) {
//                var e = ((Ellipse) HostCanvas.Children[i]);
//                e.Width = e.Height = (c.radius * 2) - 2;
//                e.Fill = new SolidColorBrush(c.circleColor);
//                Canvas.SetLeft(e, c.x - c.radius);
//                Canvas.SetTop(e, c.y - c.radius);
//            } else {
//                Ellipse e = new Ellipse();
//                e.Width = e.Height = (c.radius * 2) - 2;
//                e.Fill = new SolidColorBrush(c.circleColor);
//                Canvas.SetLeft(e, c.x - c.radius);
//                Canvas.SetTop(e, c.y - c.radius);
//                HostCanvas.Children.Add(e);
//            }
        }

        // in case the number of circles have dropped
//        for (int i = allCircles.size(); i < HostCanvas.Children.Count; i++) {
//            HostCanvas.Children.RemoveAt(AllCircles.Count);
//        }
    }

    private int dp(int x) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                x, getContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircles(canvas);
    }

    private class Circle {
        private float x, y, radius;

        Circle(float x, float y, float radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public float d2Point(PointF center) {
            float dx = x - center.x;
            float dy = y - center.y;
            return (int) Math.sqrt(dx * dx + dy * dy);
        }
    }
}
