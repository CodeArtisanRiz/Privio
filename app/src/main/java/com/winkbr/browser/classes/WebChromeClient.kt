package com.winkbr.browser.classes

import android.webkit.WebView

class WebChromeClient : android.webkit.WebChromeClient() {


    override fun onProgressChanged(view: WebView?, progress: Int) {
        super.onProgressChanged(view, progress)

        val webView = view as com.winkbr.browser.activity.WebView
        webView.progressBar?.progress = progress



    }
}
