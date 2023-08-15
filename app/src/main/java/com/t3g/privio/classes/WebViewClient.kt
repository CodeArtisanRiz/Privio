package com.t3g.privio.classes

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.t3g.privio.Database
import com.t3g.privio.R
import com.t3g.privio.activity.TabActivity
import com.t3g.privio.tabs.TabInfo
import kotlinx.android.synthetic.main.activity_tab.*
import kotlinx.android.synthetic.main.settings_activity.view.*


class WebViewClient : android.webkit.WebViewClient() {
//    var preferences: SharedPreferences? = null
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        val webView = view as com.t3g.privio.activity.WebView
        webView.progressBar?.isVisible = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.savePassword = true
        webView.requestFocus()
        val topImageView : ImageView = TabInfo.activity.backgroundImg
        val address: AutoCompleteTextView = TabInfo.activity.address_bar
        val tabIco: TextView = TabInfo.activity.tabCount


        if (webView.url!!.startsWith("file")) {
            address.setText("")
            address.hint = "Search or enter URL"
            address.isEnabled = true
        }
        else if (webView.url!!.startsWith("https://winkbr-browser.web.app")) {
            tabIco.visibility = View.GONE
            topImageView.visibility = View.VISIBLE
            address.setText("")
            address.hint = "Search or enter URL"
            address.isEnabled = true
        }
        else if(webView.url!!.startsWith("http://13.127.225.49/")) {
            tabIco.visibility = View.VISIBLE
            topImageView.visibility = View.GONE
            address.setText("")
            address.hint = "VPN Mode"
            address.isEnabled = false
        }
        else if(webView.url!!.startsWith("https://hczhcz.github.io/2048/20ez/")) {
            tabIco.visibility = View.GONE
            topImageView.visibility = View.GONE
            address.setText("")
            address.hint = "Game Mode"
            address.isEnabled = false
        }
        else if(webView.url!!.startsWith("https://doodlecricket.github.io/")) {
            tabIco.visibility = View.GONE
            topImageView.visibility = View.GONE
            address.setText("")
            address.hint = "Game Mode"
            address.isEnabled = false
        }
        else if(webView.url!!.startsWith("https://hextris.io")) {
            tabIco.visibility = View.GONE
            topImageView.visibility = View.GONE
            address.setText("")
            address.hint = "Game Mode"
            address.isEnabled = false
        }
        else if(webView.url!!.startsWith("https://nebezb.com/floppybird/")) {
            tabIco.visibility = View.GONE
            topImageView.visibility = View.GONE
            address.setText("")
            address.hint = "Game Mode"
            address.isEnabled = false
        }
        else if(webView.url!!.startsWith("http")) {
            tabIco.visibility = View.VISIBLE
            topImageView.visibility = View.GONE
            address.setText(webView.url!!.toString())
            address.isEnabled = true
        }

    }

    @SuppressLint("ResourceAsColor")
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)


        val webView = view as com.t3g.privio.activity.WebView
        webView.progressBar?.isVisible = false
        val address: AutoCompleteTextView = (TabInfo.activity as TabActivity).address_bar



        if (webView.url!!.startsWith("file")) {
            address.setText("")
            address.hint = "Search or enter URL"
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.visibility = View.INVISIBLE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        }
//        else if (webView.url!!.startsWith("https://winkrbr-home.web.app")) {
//
//            address.setText("")
//            address.hint = "Search or enter URL"
//            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
//            statusIcon.visibility = View.INVISIBLE
//            statusIcon.setImageResource(R.drawable.ic_home)
//            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation
//
//        }
        else if (webView.url!!.startsWith("https://winkbr-browser.web.app")) {

            address.setText("")
            address.hint = "Search or enter URL"
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.visibility = View.INVISIBLE
            statusIcon.setImageResource(R.drawable.ic_home)
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if(webView.url!!.startsWith("http://13.127.225.49/")) {
            address.setText("")
            address.hint= "VPN Mode"
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.setImageResource(R.drawable.incognito)
            statusIcon.visibility = View.VISIBLE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if (webView.url!!.startsWith("https://hczhcz.github.io/2048/20ez/")) {
            address.setText("")
            address.hint = "Game Mode"
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.setImageResource(R.drawable.ic_game)
            statusIcon.visibility = View.VISIBLE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if (webView.url!!.startsWith("https://doodlecricket.github.io/")) {
            address.setText("")
            address.hint = "Game Mode"
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.setImageResource(R.drawable.ic_game)
            statusIcon.visibility = View.VISIBLE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if (webView.url!!.startsWith("https://hextris.io")) {
            address.setText("")
            address.hint = "Game Mode"
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.setImageResource(R.drawable.ic_game)
            statusIcon.visibility = View.VISIBLE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if (webView.url!!.startsWith("https://nebezb.com/floppybird/")) {
            address.setText("")
            address.hint = "Game Mode"
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.setImageResource(R.drawable.ic_game)
            statusIcon.visibility = View.VISIBLE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if(webView.url!!.startsWith("https")) {
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.setImageResource(R.drawable.ic_lock)
            statusIcon.visibility = View.VISIBLE
            address.setText(webView.url!!.toString())
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation

        } else if(webView.url!!.startsWith("http")) {
            address.setText(webView.url!!.toString())
            val statusIcon: ImageView = (TabInfo.activity as TabActivity).statusIcon
            statusIcon.setImageResource(R.drawable.ic_lock)
            statusIcon.visibility = View.VISIBLE
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation
        }
        else{
            val btm_nav: BottomNavigationView = (TabInfo.activity as TabActivity).bottom_navigation
        }




        val history = History(null, webView.title.toString(), webView.url!!, System.currentTimeMillis())
        when {
            webView.url!!.startsWith("file") -> {}
//            webView.url!!.startsWith("https://winkrbr-home.web.app") -> {}
            webView.url!!.startsWith("https://winkbr-browser.web.app") -> {}
            webView.url!!.startsWith("http://13.127.225.49") -> {}
            webView.url!!.startsWith("https://hczhcz.github.io/2048/20ez/") -> {}
            webView.url!!.startsWith("https://doodlecricket.github.io/") -> {}
            webView.url!!.startsWith("https://hextris.io") -> {}
            webView.url!!.startsWith("https://nebezb.com/floppybird/") -> {}
            else -> {
                address.isEnabled = true
                AsyncTask.execute {
                    Database.db?.historyDao()?.insert(history)
                }
            }
        }
    }
    fun clearCache(view: WebView?){
        val webViewA = view as com.t3g.privio.activity.WebView
        webViewA.settings.setAppCacheEnabled(true)
        webViewA.clearCache(true)
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()

    }



}