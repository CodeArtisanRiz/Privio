package com.winkbr.browser.activity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.AttributeSet
import android.util.Patterns
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.winkbr.browser.Database
import com.winkbr.browser.classes.Bookmark
import com.winkbr.browser.classes.WebChromeClient
import com.winkbr.browser.classes.WebViewClient
import com.winkbr.browser.tabs.TabInfo
import kotlinx.android.synthetic.main.view_tab.view.*

@Suppress("DEPRECATION")
class WebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedWebView(context, attrs, defStyleAttr) {

    lateinit var activity: TabActivity
    var progressBar: ProgressBar? = null


    init {
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()

        initSettings()

        setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return@setDownloadListener
            }

            val request = DownloadManager.Request(Uri.parse(url))
            val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
            val cookies = CookieManager.getInstance().getCookie(url)

            request.setMimeType(mimeType)
            request.addRequestHeader("cookie", cookies)
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading started")
            request.setTitle(fileName)
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            val manager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
            Toast.makeText(
                context, "Download started",
                Toast.LENGTH_SHORT).show()

            val webView = TabInfo.currentWebView()
            val downloads = Bookmark(fileName, Environment.DIRECTORY_DOWNLOADS + "/" + fileName)
            AsyncTask.execute {
                Database.db?.bookmarkDao()?.insert(downloads)
            }
        }
    }

    fun loadHome() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        loadUrl("https://winkbr-browser.web.app")
    }



    fun search(searchQuery: String) {
        var pQuery = searchQuery
        if (Patterns.WEB_URL.matcher(pQuery).matches()) {
            if (pQuery.startsWith("http:")) {
                pQuery = pQuery
            }
            if (pQuery.startsWith("https:")) {
                pQuery = pQuery
            }
            if (pQuery.startsWith("https://m.")) {
                pQuery = pQuery
            }
            if (pQuery.startsWith("http://m.")) {
                pQuery = pQuery
            }
            if (pQuery.startsWith("m.")) {
                pQuery = "http://$pQuery"
            } else {
                if (!pQuery.startsWith("http")) {
                    pQuery = "http://$pQuery"
                }
            }
            web_view.loadUrl(pQuery)
        } else
            web_view.loadUrl("https://www.google.com//search?q=+$pQuery")


    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initSettings() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        val userAgentList = settings.userAgentString.split(" ").toMutableList()
        userAgentList.add(userAgentList.size - 1, "pBrowse")
        val defaultUserAgent = userAgentList.joinToString (separator = " ") { it -> it }
        var prefUserAgent = preferences.getString("user_agent", defaultUserAgent)
        if (prefUserAgent.isNullOrBlank()) prefUserAgent = defaultUserAgent

        settings.apply {
            javaScriptEnabled = preferences.getBoolean("javascript", true)
            blockNetworkImage = !preferences.getBoolean("load_image", true)
            useWideViewPort = preferences.getBoolean("viewport", true)
            saveFormData = preferences.getBoolean("form_data", true)
            userAgentString = prefUserAgent
            setGeolocationEnabled(preferences.getBoolean("location", true))
//            builtInZoomControls = preferences.getBoolean("zoom", true)
//            builtInZoomControls = false
        }

        CookieManager.getInstance().setAcceptCookie(preferences.getBoolean("accept_cookies", true))
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, preferences.getBoolean("3rd_party_cookies", false))
    }
}