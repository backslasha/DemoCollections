package yhb.dc.demo.fragment.fragment_dialog

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.ProgressBar
import yhb.dc.demo.network.webview.WebViewPool

/**
 * Created by yhb on 18-6-12.
 */

class WebDialog : androidx.fragment.app.DialogFragment() {

    private lateinit var mProgressBar: ProgressBar
    private lateinit var mWebView: WebView
    private var mUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUrl = arguments?.getString(ARG_URL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val ll = LinearLayout(activity)
        ll.orientation = LinearLayout.VERTICAL
        ll.addView({
            mProgressBar = ProgressBar(activity)
            mProgressBar
        }())
        ll.addView({
            mWebView = WebViewPool.getsWebView(activity?.applicationContext)
            setupWebView(mWebView)
            mWebView
        }(), LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        return ll
    }

    override fun onResume() {
        super.onResume()
        mWebView.loadUrl(mUrl)
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0)
    }


    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView(webView: WebView) {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    mProgressBar.visibility = View.INVISIBLE
                } else {
                    mProgressBar.visibility = View.VISIBLE
                    mProgressBar.progress = newProgress
                }
            }

            override fun onReceivedTitle(webView: WebView, title: String) {

            }

        }
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true// 设置允许JS弹窗
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = dialog?.window
        if (window != null) {
            val lp = window.attributes
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT
            window.attributes = lp
        }
    }


    companion object {

        private const val ARG_URL = "ARG_URL"

        fun newInstance(url: String): WebDialog {
            val args = Bundle()
            val fragment = WebDialog()
            fragment.arguments = args
            args.putString(ARG_URL, url)
            return fragment
        }
    }

}
