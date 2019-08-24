package yhb.dc.demo.customview.custom_view.carousel;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.common.DemoBaseActivity;

public class CarouselActivity extends DemoBaseActivity implements Demo {

    private CarouselView mCarouselView;
    private static final String TAG = CarouselActivity.class.getCanonicalName();


    private static final String[] urls = {
            "https://www.google.co.jp/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
            "https://www.google.co.jp/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
            "https://www.google.co.jp/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
//            "https://mobile.likee.video/live/act-14330/window.html",
//            "https://mobile.likee.video/live/act_14168/window.html",
//            "https://mobile.likee.video/live/act-13802/webview.html"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        mCarouselView = findViewById(R.id.carousel_vp);
        mCarouselView.setViewProvider(new CarouselView.ViewProvider() {
            @SuppressLint("SetTextI18n")
            @Override
            public View createView(int position) {
//
//                if (position == 4) {
////                    FrameLayout frameLayout = new FrameLayout(CarouselActivity.this);
////                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
////                    lp.gravity = Gravity.CENTER;
////                    final ContentLoadingProgressBar progressBar =
////                            (ContentLoadingProgressBar) LayoutInflater.from(CarouselActivity.this).inflate(R.layout.item_progresss_bar, null);
////                    progressBar.post(new Runnable() {
////                        @Override
////                        public void run() {
////                            progressBar.getIndeterminateDrawable().setColorFilter(
////                                    ContextCompat.getColor(CarouselActivity.this,
////                                    android.R.color.white), PorterDuff.Mode.MULTIPLY);
////                        }
////                    });
////                    frameLayout.addView(progressBar, lp);
//                    ProgressBar progressBar = new ProgressBar(CarouselActivity.this, null, android.R.attr.progressBarStyleSmall);
//                    progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
//                    return progressBar;
//                }
//
//                final WebView webView = new WebView(CarouselActivity.this);
//                webView.loadUrl(urls[position % urls.length]);
//                webView.setBackgroundColor(Color.BLUE);
//                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//                webView.setBackgroundColor(Color.TRANSPARENT);
//                webView.setVisibility(View.GONE);
//                webView.setWebViewClient(new WebViewClient() {
//                    private AtomicInteger retryCount = new AtomicInteger(0);
//
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        super.onPageFinished(view, url);
//                        Log.d(TAG, "page finish @ " + retryCount.addAndGet(1));
//                        if (retryCount.get() < 5) {
//                            webView.reload();
//                        }
//                        view.setVisibility(View.VISIBLE);
//                    }
//                });
//                webView.setHorizontalScrollBarEnabled(false);
//                webView.setVerticalScrollBarEnabled(false);
//                return webView;

                TextView textView = new TextView(CarouselActivity.this);
                textView.setGravity(Gravity.CENTER);
                textView.setText(String.valueOf(position));
                textView.setBackgroundColor(getColor0(position));
                return textView;
            }

            private int getColor0(int position) {
                switch (position) {
                    case 0:
                        return Color.GREEN;
                    case 1:
                        return Color.RED;
                    case 2:
                        return Color.YELLOW;
                }
                return 0;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        mCarouselView.startPlay();
    }

    public void onClick1(View view) {
        mCarouselView.startPlay();
    }

    public void onClick2(View view) {
        mCarouselView.stopPlay();
    }
}
