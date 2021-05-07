package com.winkbr.browser.classes

import android.webkit.WebView

class WebChromeClient : android.webkit.WebChromeClient() {


    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)

        val webView = view as com.winkbr.browser.activity.WebView
        webView.progressBar?.progress = newProgress

    }
}
