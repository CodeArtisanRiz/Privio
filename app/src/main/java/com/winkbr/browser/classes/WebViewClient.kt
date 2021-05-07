package com.winkbr.browser.classes

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.winkbr.browser.Database
import com.winkbr.browser.activity.TabActivity
import com.winkbr.browser.tabs.TabInfo
import kotlinx.android.synthetic.main.activity_tab.*


class WebViewClient : android.webkit.WebViewClient() {
    var preferences: SharedPreferences? = null

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)



        val webView = view as com.winkbr.browser.activity.WebView
        webView.progressBar?.isVisible = true

        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
//        webView.settings.useWideViewPort = false
//        webView.settings.loadWithOverviewMode = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.setAppCacheEnabled(false)
        webView.clearCache(true)

        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()

        val address: AutoCompleteTextView = (TabInfo.activity as TabActivity).address_bar
        if (webView.url.startsWith("file")) {
            address.setText("")
            address.hint = "Search or enter URL"
            address.isEnabled = true
        }
        else if (webView.url.startsWith("https://winkrbr-home.web.app")) {
            address.setText("")
            address.hint = "Search or enter URL"
            address.isEnabled = true
        }
        else if(webView.url.startsWith("http://13.127.225.49/")) {
            address.setText("")
            address.hint = "VPN Mode"
            address.isEnabled = false
        }
        else if(webView.url.startsWith("https://hczhcz.github.io/2048/20ez/")) {
            address.setText("")
            address.hint = "Game Mode"
            address.isEnabled = false
        }
        else if(webView.url.startsWith("https://doodlecricket.github.io/")) {
            address.setText("")
            address.hint = "Game Mode"
            address.isEnabled = false
        }
        else if(webView.url.startsWith("https://hextris.io")) {
            address.setText("")
            address.hint = "Game Mode"
            address.isEnabled = false
        }
        else if(webView.url.startsWith("https://nebezb.com/floppybird/")) {
            address.setText("")
            address.hint = "Game Mode"
            address.isEnabled = false
        }
        else if(webView.url.startsWith("http")) {
            address.setText(webView.url.toString())
            address.isEnabled = true
        }

    }

    @SuppressLint("ResourceAsColor")
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)


        val webView = view as com.winkbr.browser.activity.WebView
        webView.progressBar?.isVisible = false
        val address: AutoCompleteTextView = (TabInfo.activity as TabActivity).address_bar



        if (webView.url.startsWith("file")) {
            address.setText("")
            address.hint = "Search or enter URL"
            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.GONE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.GONE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.VISIBLE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.GONE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        }
        else if (webView.url.startsWith("https://winkrbr-home.web.app")) {
            address.setText("")
            address.hint = "Search or enter URL"
            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.GONE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.GONE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.VISIBLE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.GONE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if(webView.url.startsWith("http://13.127.225.49/")) {
            address.setText("")
            address.hint= "VPN Mode"

            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.GONE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.GONE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.GONE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.VISIBLE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if (webView.url.startsWith("https://hczhcz.github.io/2048/20ez/")) {
            address.setText("")
            address.hint = "Search or enter URL"
            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.GONE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.GONE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.GONE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.GONE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if (webView.url.startsWith("https://doodlecricket.github.io/")) {
            address.setText("")
            address.hint = "Search or enter URL"
            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.GONE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.GONE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.GONE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.GONE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if (webView.url.startsWith("https://hextris.io")) {
            address.setText("")
            address.hint = "Search or enter URL"
            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.GONE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.GONE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.GONE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.GONE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if (webView.url.startsWith("https://nebezb.com/floppybird/")) {
            address.setText("")
            address.hint = "Search or enter URL"
            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.GONE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.GONE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.GONE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.GONE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if(webView.url.startsWith("https")) {

            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.VISIBLE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.GONE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.GONE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.GONE
            address.setText(webView.url.toString())
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if(webView.url.startsWith("http")) {
            address.setText(webView.url.toString())
            val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
            sslIco.visibility = View.GONE
            val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
            noSslIco.visibility = View.VISIBLE
            val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
            homeIco.visibility = View.GONE
            val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
            proxyIco.visibility = View.GONE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation
        }
        else{
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation
        }




        val history = History(null, webView.title, webView.url, System.currentTimeMillis())
        when {
            webView.url.startsWith("file") -> {}
            webView.url.startsWith("https://winkrbr-home.web.app") -> {}
            webView.url.startsWith("http://13.127.225.49") -> {}
            webView.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {}
            webView.url.startsWith("https://doodlecricket.github.io/") -> {}
            webView.url.startsWith("https://hextris.io") -> {}
            webView.url.startsWith("https://nebezb.com/floppybird/") -> {}
            else -> {
                address.isEnabled = true
                AsyncTask.execute {
                    Database.db?.historyDao()?.insert(history)
                }
            }
        }
    }

}