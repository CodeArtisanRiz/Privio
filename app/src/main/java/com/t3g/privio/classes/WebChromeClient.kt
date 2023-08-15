package com.t3g.privio.classes

import android.webkit.WebView

class WebChromeClient : android.webkit.WebChromeClient() {


    override fun onProgressChanged(view: WebView?, progress: Int) {
        super.onProgressChanged(view, progress)

        val webView = view as com.t3g.privio.activity.WebView
        webView.progressBar?.progress = progress



    }
}
