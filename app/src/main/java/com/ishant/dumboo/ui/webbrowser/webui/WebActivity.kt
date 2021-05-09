package com.ishant.dumboo.ui.webbrowser.webui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ishant.dumboo.R
import com.ishant.dumboo.databinding.ActivityWebBinding


class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_web)
        var url = getIntent().getStringExtra("url")
        if (TextUtils.isEmpty(url)) {
            finish();
        }
        initWebView()
        binding.webView.loadUrl(url!!);
    }
    private fun initWebView() {
        binding.webView.setWebChromeClient(MyWebChromeClient(this))
        binding.webView.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progress.setVisibility(View.VISIBLE)
                invalidateOptionsMenu()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                binding.webView.loadUrl(url!!)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progress.setVisibility(View.GONE)
                invalidateOptionsMenu()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                binding.progress.setVisibility(View.GONE)
                invalidateOptionsMenu()
            }
        })
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.webView.getSettings().setJavaScriptEnabled(true)
        binding.webView.setHorizontalScrollBarEnabled(false)

    }
    inner class MyWebChromeClient(context: Context) : WebChromeClient() {
        var context: Context

        init {
            this.context = context
        }
    }

    override fun onBackPressed() {
        finish()
    }
}