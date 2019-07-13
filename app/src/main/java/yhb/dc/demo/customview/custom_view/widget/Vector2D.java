package yhb.dc.demo.customview.custom_view.widget;

/**
 * Created by yhb on 18-7-24.
 */

public class Vector2D {
    public float x1, x2, y1, y2;

    public Vector2D(float x1, float x2, float y1, float y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public float componentX() {
        return x2 - x1;
    }

    public float componentY() {
        return y2 - y1;
    }
}
