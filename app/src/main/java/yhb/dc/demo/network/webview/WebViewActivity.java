package yhb.dc.demo.network.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;
import yhb.dc.common.Demo;

import static yhb.dc.demo.network.webview.WebViewMainActivity.EXTRA_RECORD;

@Demo
public class WebViewActivity extends AppCompatActivity {

    private static final String EXTRA_REUSE_WEB_VIEW = "extra_reuse_web_view";

    private EditText mEditText;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private boolean mReuseWebView = false;
    private FrameLayout mFrameLayout;

    private long start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        if (getIntent() != null) {
            mReuseWebView = getIntent().getBooleanExtra(EXTRA_REUSE_WEB_VIEW, false);
        }
        start = System.currentTimeMillis();

        mFrameLayout = findViewById(R.id.frame_layout_web_view);

        if (mReuseWebView) {
            mFrameLayout.addView(WebViewPool.getsWebView(this), 0);
        } else {
            mFrameLayout.addView(LayoutInflater.from(this).inflate(R.layout.include_web_view, mFrameLayout, false), 0);
        }

//        long end = System.currentTimeMillis();
//        Intent intent = new Intent().putExtra(EXTRA_RECORD, new WebViewMainActivity.Record(start, end, end - start, ""));
//        WebViewActivity.this.setResult(RESULT_OK, intent);
//        WebViewActivity.this.finish();

        mWebView = findViewById(R.id.web_view);

        setupWebView(mWebView);

        mProgressBar = findViewById(R.id.fragment_photo_page_progress_bar);
        mProgressBar.setMax(100);
        mEditText = findViewById(R.id.edit_text);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“GO”键
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm != null && imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    // 对应逻辑操作
                    loadUrl();
                    return true;
                }
                return false;
            }
        });

        mEditText.setText("https://support.qq.com/product/25114?d-wx-push=1");

        loadUrl();
    }

    private void loadUrl() {
        String url = String.valueOf(mEditText.getText());
        if (!url.startsWith("http") && !url.startsWith("file") && !url.startsWith("content") && !url.startsWith("ftp")) {
            url = "http://" + url;
        }



        if (URLUtil.isValidUrl(url)) {
            mWebView.loadUrl(url);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setupWebView(final WebView webView) {

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView webView, String title) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(title);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }


        });

        webView.setWebViewClient(new WebViewClient() {
            List<WebViewMainActivity.Record> mRecords = new ArrayList<>();

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                long end = System.currentTimeMillis();
                Intent intent = new Intent().putExtra(EXTRA_RECORD, new WebViewMainActivity.Record(start, end, end - start, url));
                WebViewActivity.this.setResult(RESULT_OK, intent);
                WebViewActivity.this.finish();
            }

        });

        webView.addJavascriptInterface(new AndroidtoJs(), "test");// AndroidtoJS 类对象映射到 js 的 test 对象

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置允许JS弹窗

    }


    public void callJs(View view) {
        mWebView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
            }
        });
//            mWebView.loadUrl("javascript:callJS()");
        //

    }

    public static Intent newIntent(Context context, boolean reuseWebView) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_REUSE_WEB_VIEW, reuseWebView);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFrameLayout.removeAllViews();
    }
}
