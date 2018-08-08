package yhb.dc.demo.network.webview;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebView;

import yhb.dc.R;

public class WebViewPool {
    static WebView sWebView;

    public static WebView getsWebView(Context context) {
        if (sWebView == null) {
            sWebView = (WebView) LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.include_web_view, null);
        }
        return sWebView;
    }


}
