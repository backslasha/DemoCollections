package yhb.dc.demo.customview.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;
import yhb.dc.common.Demo;


@Demo
public class CanvasActivity extends AppCompatActivity  {

    private android.support.v7.widget.Toolbar mToolbar;
    private Spinner mSpinner;
    private List<View> mViews;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        mViews = new ArrayList<>();
        mViews.add(new BitmapMeshView(this, null));
        mViews.add(new BitmapShaderView(this, null));
        mViews.add(new CanvasView(this, null));
        mViews.add(new ClipRectView(this, null));
        mViews.add(new PathView(this, null));
        mViews.add(new ShaderView(this, null));
        mViews.add(new QuadToView(this, null));
        mViews.add(new FoldView(this, null));

        PageView pageView = new PageView(this, null);
        Bitmap[] bitmaps = new Bitmap[3];
        int screenWidth = getScreenWidth()/4;
        int screenHeight = getScreenHeight()/4;
        bitmaps[0] = loadBitmap(this, R.drawable.screen, screenWidth, screenHeight);
        bitmaps[1] = loadBitmap(this, R.drawable.screen1, screenWidth, screenHeight);
        bitmaps[2] = loadBitmap(this, R.drawable.screen2, screenWidth, screenHeight);
        pageView.setBitmaps(bitmaps);
        mViews.add(pageView);

        final ViewGroup root = (ViewGroup) ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);

        mSpinner = findViewById(R.id.spinner);
        mSpinner.setSelected(false);

        List<CharSequence> views = new ArrayList<>();
        for (View view : mViews) {
            views.add(view.getClass().getSimpleName());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this,
                android.R.layout.simple_spinner_item,
                views

        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CanvasActivity.this, "selected " + position, Toast.LENGTH_SHORT).show();
                if (root.getChildCount() == 2) {
                    root.removeView(root.getChildAt(1));
                }
                root.addView(mViews.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private int getScreenWidth() {
        DisplayMetrics outMetric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetric);
        return outMetric.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics outMetric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetric);
        return outMetric.heightPixels;
    }

    public static Bitmap loadBitmap(Context context, @DrawableRes int resId, int targetW, int targetH) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        int bh = bitmap.getHeight();
        int bw = bitmap.getWidth();
        int inSampleSize = 1;
        while (bh / 2 > targetH && bw / 2 > targetW) {
            inSampleSize *= 2;
            bh = bh / 2;
            bw = bw / 2;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return bitmap;
    }
}
