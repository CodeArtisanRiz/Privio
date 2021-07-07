package com.winkbr.browser.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.net.sip.SipSession
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.winkbr.browser.Database
import com.winkbr.browser.R
import com.winkbr.browser.classes.Tab
import com.winkbr.browser.tabs.TabInfo
import com.winkbr.browser.tabs.TabInfo.activity
import kotlinx.android.synthetic.main.activity_tab.*
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.view_tab.*
import kotlinx.android.synthetic.main.view_tab.view.*
import java.util.*
import kotlin.math.abs


class TabActivity : AppCompatActivity() {

    var forwardButton: MenuItem? = null
    var countButton: TextView? = null
    var imgNewTab: ImageView? = null
    var imgAddBookmark: ImageView? = null
    var imgFindInPage: ImageView? = null
    var imgSource: ImageView? = null
    var imgPrint: ImageView? = null
    var imgDestop: ImageView? = null
    var imgBack: ImageView? = null
    var imgForward: ImageView? = null
    var imgReload: ImageView? = null
    var imgShare: ImageView? = null
    var bgImage: ImageView? = null
    var news_btn: Button? = null
    var appBarLayout: AppBarLayout? = null

    @SuppressLint("CutPasteId", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.initDb(this)
        TabInfo.activity = this
        setContentView(R.layout.activity_tab)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        countButton = findViewById(R.id.tabCount)
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
        appBarLayout = findViewById<View>(R.id.appBarLayout) as AppBarLayout?
        appBarLayout?.isActivated = true
        appBarLayout?.setExpanded(true, true)
        val tabIco: TextView = findViewById(R.id.tabCount)
        val statusIco: ImageView = findViewById(R.id.statusIcon)

        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                abs(verticalOffset) - appBarLayout.totalScrollRange == 0 -> {
//                  Collapsed
//                  Toast.makeText(applicationContext,"Collapsed", Toast.LENGTH_SHORT).show()
                    tabIco.visibility = View.VISIBLE
                    statusIco.visibility = View.VISIBLE
                }
                verticalOffset == 0 -> {
//                  Expanded
//                  Toast.makeText(applicationContext,"Expanded", Toast.LENGTH_SHORT).show()
                    tabIco.visibility = View.INVISIBLE
                    statusIco.visibility = View.INVISIBLE
                }
                else -> {
//                  Idle
//                  Toast.makeText(applicationContext,"idle", Toast.LENGTH_SHORT).show()
                }
            }
        }
        )

        loadImg()
        val extras = intent.extras
        if (extras != null) {
            val externalUrl: Uri? = intent?.data
            val url = intent.extras!!.getString("query")
            if (url.toString().startsWith("http")) {
                TabInfo.addTab(url.toString())
                intent.removeExtra("query")
            }
            else {
                TabInfo.addTab(externalUrl.toString())
            }
        }


        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_back -> {
                    when {
                        TabInfo.currentWebView().canGoBack() -> {
                            TabInfo.currentWebView().goBack()
                        }
                        else -> {
                        }
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_forward -> {

                    when {
                        TabInfo.currentWebView().canGoForward() -> {
                            TabInfo.currentWebView().goForward()
                        }
                        else -> {
                        }
                    }

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_menu -> {
                    when {
                        (web_view.url.toString().startsWith("https://winkbr-browser.web.app")) -> {
                            val bottomSheetDialog = BottomSheetDialog(
                                this,
                                R.style.BottomSheetDialogTheme
                            )
                            val bottomSheetView = layoutInflater.inflate(
                                R.layout.layout_bottom_sheet_disabled, null
                            )

//                    Bookmarks
                            bottomSheetView.findViewById<View>(R.id.show_bookmarks)
                                .setOnClickListener {
                                    val intent = Intent(this, BookmarkActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    History
                            bottomSheetView.findViewById<View>(R.id.show_history)
                                .setOnClickListener {
                                    val intent = Intent(this, HistoryActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Downloads
                            bottomSheetView.findViewById<View>(R.id.show_downloads)
                                .setOnClickListener {
                                    val intent = Intent(this, DownloadActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Settings
                            bottomSheetView.findViewById<View>(R.id.settings).setOnClickListener {
                                val intent = Intent(this, SettingsActivity::class.java)
                                startActivity(intent)
                                bottomSheetDialog.dismiss()
                            }
//                    Add Tab
                            bottomSheetView.findViewById<View>(R.id.new_tab).setOnClickListener {
                                addTab()
                                bottomSheetDialog.dismiss()
                            }
//                    New Private Tab
                            bottomSheetView.findViewById<View>(R.id.new_private_tab).setOnClickListener {

                                addProxyTab()

                                bottomSheetDialog.dismiss()
                            }
//                    Add Bookmark
                            bottomSheetView.findViewById<View>(R.id.add_bookmark)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }

                                        web_view.url.startsWith("http") -> {
                                            val message: String
                                            val sharedPreferences = getSharedPreferences(
                                                PREFERENCES,
                                                MODE_PRIVATE
                                            )
                                            val jsonLink =
                                                sharedPreferences.getString(WEB_LINKS, null)
                                            val jsonTitle =
                                                sharedPreferences.getString(WEB_TITLE, null)
                                            message = if (jsonLink != null && jsonTitle != null) {
                                                val gson = Gson()
                                                val linkList = gson.fromJson<ArrayList<String>>(
                                                    jsonLink,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                val titleList = gson.fromJson<ArrayList<String>>(
                                                    jsonTitle,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                if (linkList.contains(web_view!!.url.toString())) {
                                                    "Bookmark exits"
                                                } else {
                                                    linkList.add(web_view!!.url.toString())
                                                    titleList.add(web_view!!.title.trim { it <= ' ' })
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString(
                                                        WEB_LINKS,
                                                        Gson().toJson(linkList)
                                                    )
                                                    editor.putString(
                                                        WEB_TITLE,
                                                        Gson().toJson(titleList)
                                                    )
                                                    editor.apply()
                                                    "Bookmark added"
                                                }
                                            } else {
                                                val linkList = ArrayList<String>()
                                                val titleList = ArrayList<String>()
                                                linkList.add(web_view!!.url.toString())
                                                titleList.add(web_view!!.title)
                                                val editor = sharedPreferences.edit()
                                                editor.putString(WEB_LINKS, Gson().toJson(linkList))
                                                editor.putString(
                                                    WEB_TITLE,
                                                    Gson().toJson(titleList)
                                                )
                                                editor.apply()
                                                "Bookmark added"
                                            }
                                            makeText(
                                                this@TabActivity,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            bottomSheetDialog.dismiss()
                                        }


                                    }

                                }

//                    Find in Page
                            bottomSheetView.findViewById<View>(R.id.find_in_page)
                                .setOnClickListener {
                                    val sharingUrl: String = web_view.url.toString()
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        else -> {
//                              find in page Dialog
                                            web_view.showFindDialog("", true)
                                            bottomSheetDialog.setContentView(bottomSheetView)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    View Source
                            bottomSheetView.findViewById<View>(R.id.page_source)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            web_view.settings.useWideViewPort = false
                                            val sourceMobileUrl: String = web_view.url.toString()
                                            web_view.loadUrl("view-source:$sourceMobileUrl")
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Print
                            bottomSheetView.findViewById<View>(R.id.print).setOnClickListener {
                                when {

                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        bottomSheetDialog.dismiss()
                                    }
                                    web_view.url.startsWith("http") -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                    else -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                }
                            }
//                    Desktop Site
                            bottomSheetView.findViewById<View>(R.id.desktop_site)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {

                                        }
                                        web_view.url.startsWith("http") -> {
//                                            web_view.settings.useWideViewPort = true
                                            val sourceUrl: String = web_view.url.toString()

                                            web_view.settings.loadWithOverviewMode = true
                                            web_view.settings.useWideViewPort = true
                                            web_view.settings.setSupportZoom(true)
                                            web_view.settings.builtInZoomControls = true
                                            web_view.settings.displayZoomControls = false
//                                            web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
                                            web_view.isScrollbarFadingEnabled = false
                                            val newUA =
                                                "Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0"
                                            web_view.settings.userAgentString = newUA

                                            web_view.loadUrl(sourceUrl)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Feedback
                            bottomSheetView.findViewById<View>(R.id.feedback).setOnClickListener {
                                val emailIntent = Intent(
                                    Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", "support@winkbr.com", null
                                    )
                                )
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                                startActivity(Intent.createChooser(emailIntent, "Send email..."))
                                bottomSheetDialog.dismiss()
                                bottomSheetDialog.dismiss()
                            }
//                    Exit
                            bottomSheetView.findViewById<View>(R.id.exit).setOnClickListener {
                                val dialogClickListener =
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                TabInfo.removeAllTabs()
                                                finishAffinity()
                                            }
                                            DialogInterface.BUTTON_NEGATIVE -> {
                                            }
                                        }
                                    }
                                val builder =
                                    AlertDialog.Builder(this)
                                builder.setMessage("Are you sure?")
                                    .setPositiveButton(
                                        Html.fromHtml("<font color='#000000'>Yes</font>"),
                                        dialogClickListener
                                    )
                                    .setNegativeButton(
                                        Html.fromHtml("<font color='#000000'>No</font>"),
                                        dialogClickListener
                                    )
                                    .show()

                                bottomSheetDialog.dismiss()
                            }
//                    Back
                            bottomSheetView.findViewById<View>(R.id.back).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoBack() -> {
                                        TabInfo.currentWebView().goBack()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Forward
                            bottomSheetView.findViewById<View>(R.id.forward).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoForward() -> {
                                        TabInfo.currentWebView().goForward()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Close Menu
                            bottomSheetView.findViewById<View>(R.id.close_menu).setOnClickListener {
                                bottomSheetDialog.dismiss()
                            }
//                    Reload
                            bottomSheetView.findViewById<View>(R.id.reload).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    web_view.url.startsWith("http") -> {
                                        reloadCurrentTab()
                                    }

                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Share
                            bottomSheetView.findViewById<View>(R.id.share).setOnClickListener {


                                val sharingUrl: String = web_view.url.toString()

                                if (web_view.url.startsWith("https://winkbr-browser.web.app")) {

                                } else if (web_view.url.startsWith("http")) {
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = "text/plain"
                                    intent.putExtra(Intent.EXTRA_TEXT, sharingUrl)
                                    startActivity(
                                        Intent.createChooser(
                                            intent,
                                            "Share with"
                                        )
                                    )
                                }

                                bottomSheetDialog.dismiss()
                            }

                            bottomSheetDialog.setContentView(bottomSheetView)
                            bottomSheetDialog.show()

                            return@setOnNavigationItemSelectedListener true
                        }
                        (web_view.url.toString()
                            .startsWith("https://hczhcz.github.io/2048/20ez/")) -> {
                            val bottomSheetDialog = BottomSheetDialog(
                                this,
                                R.style.BottomSheetDialogTheme
                            )
                            val bottomSheetView = layoutInflater.inflate(
                                R.layout.layout_bottom_sheet_disabled, null
                            )

//                    Bookmarks
                            bottomSheetView.findViewById<View>(R.id.show_bookmarks)
                                .setOnClickListener {
                                    val intent = Intent(this, BookmarkActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    History
                            bottomSheetView.findViewById<View>(R.id.show_history)
                                .setOnClickListener {
                                    val intent = Intent(this, HistoryActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Downloads
                            bottomSheetView.findViewById<View>(R.id.show_downloads)
                                .setOnClickListener {
                                    val intent = Intent(this, DownloadActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Settings
                            bottomSheetView.findViewById<View>(R.id.settings).setOnClickListener {
                                val intent = Intent(this, SettingsActivity::class.java)
                                startActivity(intent)
                                bottomSheetDialog.dismiss()
                            }
//                    Add Tab
                            bottomSheetView.findViewById<View>(R.id.new_tab).setOnClickListener {
                                addTab()
                                bottomSheetDialog.dismiss()
                            }
//                    New Private Tab
                            bottomSheetView.findViewById<View>(R.id.new_private_tab).setOnClickListener {

                                addProxyTab()

                                bottomSheetDialog.dismiss()
                            }
//                    Add Bookmark
                            bottomSheetView.findViewById<View>(R.id.add_bookmark)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }


                                        web_view.url.startsWith("http") -> {
                                            val message: String
                                            val sharedPreferences = getSharedPreferences(
                                                PREFERENCES,
                                                MODE_PRIVATE
                                            )
                                            val jsonLink =
                                                sharedPreferences.getString(WEB_LINKS, null)
                                            val jsonTitle =
                                                sharedPreferences.getString(WEB_TITLE, null)
                                            message = if (jsonLink != null && jsonTitle != null) {
                                                val gson = Gson()
                                                val linkList = gson.fromJson<ArrayList<String>>(
                                                    jsonLink,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                val titleList = gson.fromJson<ArrayList<String>>(
                                                    jsonTitle,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                if (linkList.contains(web_view!!.url.toString())) {
                                                    "Bookmark exits"
                                                } else {
                                                    linkList.add(web_view!!.url.toString())
                                                    titleList.add(web_view!!.title.trim { it <= ' ' })
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString(
                                                        WEB_LINKS,
                                                        Gson().toJson(linkList)
                                                    )
                                                    editor.putString(
                                                        WEB_TITLE,
                                                        Gson().toJson(titleList)
                                                    )
                                                    editor.apply()
                                                    "Bookmark added"
                                                }
                                            } else {
                                                val linkList = ArrayList<String>()
                                                val titleList = ArrayList<String>()
                                                linkList.add(web_view!!.url.toString())
                                                titleList.add(web_view!!.title)
                                                val editor = sharedPreferences.edit()
                                                editor.putString(WEB_LINKS, Gson().toJson(linkList))
                                                editor.putString(
                                                    WEB_TITLE,
                                                    Gson().toJson(titleList)
                                                )
                                                editor.apply()
                                                "Bookmark added"
                                            }
                                            makeText(
                                                this@TabActivity,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            bottomSheetDialog.dismiss()
                                        }


                                    }

                                }

//                    Find in Page
                            bottomSheetView.findViewById<View>(R.id.find_in_page)
                                .setOnClickListener {
                                    val sharingUrl: String = web_view.url.toString()
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        else -> {
//                              find in page Dialog
                                            web_view.showFindDialog("", true)
                                            bottomSheetDialog.setContentView(bottomSheetView)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    View Source
                            bottomSheetView.findViewById<View>(R.id.page_source)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            web_view.settings.useWideViewPort = false
                                            val sourceMobileUrl: String = web_view.url.toString()
                                            web_view.loadUrl("view-source:$sourceMobileUrl")
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Print
                            bottomSheetView.findViewById<View>(R.id.print).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
//                                        bottomSheetDialog.dismiss()
                                    }
                                    web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                    }
                                    web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                    }
                                    web_view.url.startsWith("https://hextris.io") -> {
                                    }
                                    web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                    }

                                    web_view.url.startsWith("http") -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                    else -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                }
                            }
//                    Desktop Site
                            bottomSheetView.findViewById<View>(R.id.desktop_site)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            val sourceUrl: String = web_view.url.toString()

                                            web_view.settings.loadWithOverviewMode = true
                                            web_view.settings.useWideViewPort = true
                                            web_view.settings.setSupportZoom(true)
                                            web_view.settings.builtInZoomControls = true
                                            web_view.settings.displayZoomControls = false
//                                            web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
                                            web_view.isScrollbarFadingEnabled = false
                                            val newUA =
                                                "Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0"
                                            web_view.settings.userAgentString = newUA

                                            web_view.loadUrl(sourceUrl)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Feedback
                            bottomSheetView.findViewById<View>(R.id.feedback).setOnClickListener {
                                val emailIntent = Intent(
                                    Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", "support@winkbr.com", null
                                    )
                                )
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                                startActivity(Intent.createChooser(emailIntent, "Send email..."))
                                bottomSheetDialog.dismiss()
                                bottomSheetDialog.dismiss()
                            }
//                    Exit
                            bottomSheetView.findViewById<View>(R.id.exit).setOnClickListener {
                                val dialogClickListener =
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                TabInfo.removeAllTabs()
                                                finishAffinity()
                                            }
                                            DialogInterface.BUTTON_NEGATIVE -> {
                                            }
                                        }
                                    }
                                val builder =
                                    AlertDialog.Builder(this)
                                builder.setMessage("Are you sure?")
                                    .setPositiveButton(
                                        Html.fromHtml("<font color='#000000'>Yes</font>"),
                                        dialogClickListener
                                    )
                                    .setNegativeButton(
                                        Html.fromHtml("<font color='#000000'>No</font>"),
                                        dialogClickListener
                                    )
                                    .show()

                                bottomSheetDialog.dismiss()
                            }
//                    Back
                            bottomSheetView.findViewById<View>(R.id.back).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoBack() -> {
                                        TabInfo.currentWebView().goBack()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Forward
                            bottomSheetView.findViewById<View>(R.id.forward).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoForward() -> {
                                        TabInfo.currentWebView().goForward()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Close Menu
                            bottomSheetView.findViewById<View>(R.id.close_menu).setOnClickListener {
                                bottomSheetDialog.dismiss()
                            }
//                    Reload
                            bottomSheetView.findViewById<View>(R.id.reload).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    web_view.url.startsWith("http") -> {
                                        reloadCurrentTab()
                                    }

                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Share
                            bottomSheetView.findViewById<View>(R.id.share).setOnClickListener {


                                val sharingUrl: String = web_view.url.toString()

                                if (web_view.url.startsWith("https://winkbr-browser.web.app")) {
                                } else if (web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/")) {
                                } else if (web_view.url.startsWith("https://doodlecricket.github.io/")) {
                                } else if (web_view.url.startsWith("https://hextris.io")) {
                                } else if (web_view.url.startsWith("https://nebezb.com/floppybird/")) {
                                } else if (web_view.url.startsWith("http")) {
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = "text/plain"
                                    intent.putExtra(Intent.EXTRA_TEXT, sharingUrl)
                                    startActivity(
                                        Intent.createChooser(
                                            intent,
                                            "Share with"
                                        )
                                    )
                                }

                                bottomSheetDialog.dismiss()
                            }

                            bottomSheetDialog.setContentView(bottomSheetView)
                            bottomSheetDialog.show()

                            return@setOnNavigationItemSelectedListener true
                        }
                        (web_view.url.toString()
                            .startsWith("https://doodlecricket.github.io/")) -> {
                            val bottomSheetDialog = BottomSheetDialog(
                                this,
                                R.style.BottomSheetDialogTheme
                            )
                            val bottomSheetView = layoutInflater.inflate(
                                R.layout.layout_bottom_sheet_disabled, null
                            )

//                    Bookmarks
                            bottomSheetView.findViewById<View>(R.id.show_bookmarks)
                                .setOnClickListener {
                                    val intent = Intent(this, BookmarkActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    History
                            bottomSheetView.findViewById<View>(R.id.show_history)
                                .setOnClickListener {
                                    val intent = Intent(this, HistoryActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Downloads
                            bottomSheetView.findViewById<View>(R.id.show_downloads)
                                .setOnClickListener {
                                    val intent = Intent(this, DownloadActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Settings
                            bottomSheetView.findViewById<View>(R.id.settings).setOnClickListener {
                                val intent = Intent(this, SettingsActivity::class.java)
                                startActivity(intent)
                                bottomSheetDialog.dismiss()
                            }
//                    Add Tab
                            bottomSheetView.findViewById<View>(R.id.new_tab).setOnClickListener {
                                addTab()
                                bottomSheetDialog.dismiss()
                            }
//                    New Private Tab
                            bottomSheetView.findViewById<View>(R.id.new_private_tab).setOnClickListener {

                                addProxyTab()

                                bottomSheetDialog.dismiss()
                            }
//                    Add Bookmark
                            bottomSheetView.findViewById<View>(R.id.add_bookmark)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }


                                        web_view.url.startsWith("http") -> {
                                            val message: String
                                            val sharedPreferences = getSharedPreferences(
                                                PREFERENCES,
                                                MODE_PRIVATE
                                            )
                                            val jsonLink =
                                                sharedPreferences.getString(WEB_LINKS, null)
                                            val jsonTitle =
                                                sharedPreferences.getString(WEB_TITLE, null)
                                            message = if (jsonLink != null && jsonTitle != null) {
                                                val gson = Gson()
                                                val linkList = gson.fromJson<ArrayList<String>>(
                                                    jsonLink,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                val titleList = gson.fromJson<ArrayList<String>>(
                                                    jsonTitle,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                if (linkList.contains(web_view!!.url.toString())) {
                                                    "Bookmark exits"
                                                } else {
                                                    linkList.add(web_view!!.url.toString())
                                                    titleList.add(web_view!!.title.trim { it <= ' ' })
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString(
                                                        WEB_LINKS,
                                                        Gson().toJson(linkList)
                                                    )
                                                    editor.putString(
                                                        WEB_TITLE,
                                                        Gson().toJson(titleList)
                                                    )
                                                    editor.apply()
                                                    "Bookmark added"
                                                }
                                            } else {
                                                val linkList = ArrayList<String>()
                                                val titleList = ArrayList<String>()
                                                linkList.add(web_view!!.url.toString())
                                                titleList.add(web_view!!.title)
                                                val editor = sharedPreferences.edit()
                                                editor.putString(WEB_LINKS, Gson().toJson(linkList))
                                                editor.putString(
                                                    WEB_TITLE,
                                                    Gson().toJson(titleList)
                                                )
                                                editor.apply()
                                                "Bookmark added"
                                            }
                                            makeText(
                                                this@TabActivity,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            bottomSheetDialog.dismiss()
                                        }


                                    }

                                }

//                    Find in Page
                            bottomSheetView.findViewById<View>(R.id.find_in_page)
                                .setOnClickListener {
                                    val sharingUrl: String = web_view.url.toString()
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        else -> {
//                              find in page Dialog
                                            web_view.showFindDialog("", true)
                                            bottomSheetDialog.setContentView(bottomSheetView)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    View Source
                            bottomSheetView.findViewById<View>(R.id.page_source)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            web_view.settings.useWideViewPort = false
                                            val sourceMobileUrl: String = web_view.url.toString()
                                            web_view.loadUrl("view-source:$sourceMobileUrl")
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Print
                            bottomSheetView.findViewById<View>(R.id.print).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
//                                        bottomSheetDialog.dismiss()
                                    }
                                    web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                    }
                                    web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                    }
                                    web_view.url.startsWith("https://hextris.io") -> {
                                    }
                                    web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                    }

                                    web_view.url.startsWith("http") -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                    else -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                }
                            }
//                    Desktop Site
                            bottomSheetView.findViewById<View>(R.id.desktop_site)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            val sourceUrl: String = web_view.url.toString()

                                            web_view.settings.loadWithOverviewMode = true
                                            web_view.settings.useWideViewPort = true
                                            web_view.settings.setSupportZoom(true)
                                            web_view.settings.builtInZoomControls = true
                                            web_view.settings.displayZoomControls = false
//                                            web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
                                            web_view.isScrollbarFadingEnabled = false
                                            val newUA =
                                                "Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0"
                                            web_view.settings.userAgentString = newUA

                                            web_view.loadUrl(sourceUrl)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Feedback
                            bottomSheetView.findViewById<View>(R.id.feedback).setOnClickListener {
                                val emailIntent = Intent(
                                    Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", "support@winkbr.com", null
                                    )
                                )
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                                startActivity(Intent.createChooser(emailIntent, "Send email..."))
                                bottomSheetDialog.dismiss()
                                bottomSheetDialog.dismiss()
                            }
//                    Exit
                            bottomSheetView.findViewById<View>(R.id.exit).setOnClickListener {
                                val dialogClickListener =
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                TabInfo.removeAllTabs()
                                                finishAffinity()
                                            }
                                            DialogInterface.BUTTON_NEGATIVE -> {
                                            }
                                        }
                                    }
                                val builder =
                                    AlertDialog.Builder(this)
                                builder.setMessage("Are you sure?")
                                    .setPositiveButton(
                                        Html.fromHtml("<font color='#000000'>Yes</font>"),
                                        dialogClickListener
                                    )
                                    .setNegativeButton(
                                        Html.fromHtml("<font color='#000000'>No</font>"),
                                        dialogClickListener
                                    )
                                    .show()

                                bottomSheetDialog.dismiss()
                            }
//                    Back
                            bottomSheetView.findViewById<View>(R.id.back).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoBack() -> {
                                        TabInfo.currentWebView().goBack()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Forward
                            bottomSheetView.findViewById<View>(R.id.forward).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoForward() -> {
                                        TabInfo.currentWebView().goForward()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Close Menu
                            bottomSheetView.findViewById<View>(R.id.close_menu).setOnClickListener {
                                bottomSheetDialog.dismiss()
                            }
//                    Reload
                            bottomSheetView.findViewById<View>(R.id.reload).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    web_view.url.startsWith("http") -> {
                                        reloadCurrentTab()
                                    }

                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Share
                            bottomSheetView.findViewById<View>(R.id.share).setOnClickListener {


                                val sharingUrl: String = web_view.url.toString()

                                if (web_view.url.startsWith("https://winkbr-browser.web.app")) {
                                } else if (web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/")) {
                                } else if (web_view.url.startsWith("https://doodlecricket.github.io/")) {
                                } else if (web_view.url.startsWith("https://hextris.io")) {
                                } else if (web_view.url.startsWith("https://nebezb.com/floppybird/")) {
                                } else if (web_view.url.startsWith("http")) {
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = "text/plain"
                                    intent.putExtra(Intent.EXTRA_TEXT, sharingUrl)
                                    startActivity(
                                        Intent.createChooser(
                                            intent,
                                            "Share with"
                                        )
                                    )
                                }

                                bottomSheetDialog.dismiss()
                            }

                            bottomSheetDialog.setContentView(bottomSheetView)
                            bottomSheetDialog.show()

                            return@setOnNavigationItemSelectedListener true
                        }
                        (web_view.url.toString().startsWith("https://hextris.io")) -> {
                            val bottomSheetDialog = BottomSheetDialog(
                                this,
                                R.style.BottomSheetDialogTheme
                            )
                            val bottomSheetView = layoutInflater.inflate(
                                R.layout.layout_bottom_sheet_disabled, null
                            )

//                    Bookmarks
                            bottomSheetView.findViewById<View>(R.id.show_bookmarks)
                                .setOnClickListener {
                                    val intent = Intent(this, BookmarkActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    History
                            bottomSheetView.findViewById<View>(R.id.show_history)
                                .setOnClickListener {
                                    val intent = Intent(this, HistoryActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Downloads
                            bottomSheetView.findViewById<View>(R.id.show_downloads)
                                .setOnClickListener {
                                    val intent = Intent(this, DownloadActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Settings
                            bottomSheetView.findViewById<View>(R.id.settings).setOnClickListener {
                                val intent = Intent(this, SettingsActivity::class.java)
                                startActivity(intent)
                                bottomSheetDialog.dismiss()
                            }
//                    Add Tab
                            bottomSheetView.findViewById<View>(R.id.new_tab).setOnClickListener {
                                addTab()
                                bottomSheetDialog.dismiss()
                            }
//                    New Private Tab
                            bottomSheetView.findViewById<View>(R.id.new_private_tab).setOnClickListener {

                                addProxyTab()

                                bottomSheetDialog.dismiss()
                            }
//                    Add Bookmark
                            bottomSheetView.findViewById<View>(R.id.add_bookmark)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }


                                        web_view.url.startsWith("http") -> {
                                            val message: String
                                            val sharedPreferences = getSharedPreferences(
                                                PREFERENCES,
                                                MODE_PRIVATE
                                            )
                                            val jsonLink =
                                                sharedPreferences.getString(WEB_LINKS, null)
                                            val jsonTitle =
                                                sharedPreferences.getString(WEB_TITLE, null)
                                            message = if (jsonLink != null && jsonTitle != null) {
                                                val gson = Gson()
                                                val linkList = gson.fromJson<ArrayList<String>>(
                                                    jsonLink,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                val titleList = gson.fromJson<ArrayList<String>>(
                                                    jsonTitle,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                if (linkList.contains(web_view!!.url.toString())) {
                                                    "Bookmark exits"
                                                } else {
                                                    linkList.add(web_view!!.url.toString())
                                                    titleList.add(web_view!!.title.trim { it <= ' ' })
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString(
                                                        WEB_LINKS,
                                                        Gson().toJson(linkList)
                                                    )
                                                    editor.putString(
                                                        WEB_TITLE,
                                                        Gson().toJson(titleList)
                                                    )
                                                    editor.apply()
                                                    "Bookmark added"
                                                }
                                            } else {
                                                val linkList = ArrayList<String>()
                                                val titleList = ArrayList<String>()
                                                linkList.add(web_view!!.url.toString())
                                                titleList.add(web_view!!.title)
                                                val editor = sharedPreferences.edit()
                                                editor.putString(WEB_LINKS, Gson().toJson(linkList))
                                                editor.putString(
                                                    WEB_TITLE,
                                                    Gson().toJson(titleList)
                                                )
                                                editor.apply()
                                                "Bookmark added"
                                            }
                                            makeText(
                                                this@TabActivity,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            bottomSheetDialog.dismiss()
                                        }


                                    }

                                }

//                    Find in Page
                            bottomSheetView.findViewById<View>(R.id.find_in_page)
                                .setOnClickListener {
                                    val sharingUrl: String = web_view.url.toString()
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        else -> {
//                              find in page Dialog
                                            web_view.showFindDialog("", true)
                                            bottomSheetDialog.setContentView(bottomSheetView)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    View Source
                            bottomSheetView.findViewById<View>(R.id.page_source)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            web_view.settings.useWideViewPort = false
                                            val sourceMobileUrl: String = web_view.url.toString()
                                            web_view.loadUrl("view-source:$sourceMobileUrl")
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Print
                            bottomSheetView.findViewById<View>(R.id.print).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
//                                        bottomSheetDialog.dismiss()
                                    }
                                    web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                    }
                                    web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                    }
                                    web_view.url.startsWith("https://hextris.io") -> {
                                    }
                                    web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                    }

                                    web_view.url.startsWith("http") -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                    else -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                }
                            }
//                    Desktop Site
                            bottomSheetView.findViewById<View>(R.id.desktop_site)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            val sourceUrl: String = web_view.url.toString()

                                            web_view.settings.loadWithOverviewMode = true
                                            web_view.settings.useWideViewPort = true
                                            web_view.settings.setSupportZoom(true)
                                            web_view.settings.builtInZoomControls = true
                                            web_view.settings.displayZoomControls = false
//                                            web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
                                            web_view.isScrollbarFadingEnabled = false
                                            val newUA =
                                                "Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0"
                                            web_view.settings.userAgentString = newUA

                                            web_view.loadUrl(sourceUrl)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Feedback
                            bottomSheetView.findViewById<View>(R.id.feedback).setOnClickListener {
                                val emailIntent = Intent(
                                    Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", "support@winkbr.com", null
                                    )
                                )
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                                startActivity(Intent.createChooser(emailIntent, "Send email..."))
                                bottomSheetDialog.dismiss()
                                bottomSheetDialog.dismiss()
                            }
//                    Exit
                            bottomSheetView.findViewById<View>(R.id.exit).setOnClickListener {
                                val dialogClickListener =
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                TabInfo.removeAllTabs()
                                                finishAffinity()
                                            }
                                            DialogInterface.BUTTON_NEGATIVE -> {
                                            }
                                        }
                                    }
                                val builder =
                                    AlertDialog.Builder(this)
                                builder.setMessage("Are you sure?")
                                    .setPositiveButton(
                                        Html.fromHtml("<font color='#000000'>Yes</font>"),
                                        dialogClickListener
                                    )
                                    .setNegativeButton(
                                        Html.fromHtml("<font color='#000000'>No</font>"),
                                        dialogClickListener
                                    )
                                    .show()

                                bottomSheetDialog.dismiss()
                            }
//                    Back
                            bottomSheetView.findViewById<View>(R.id.back).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoBack() -> {
                                        TabInfo.currentWebView().goBack()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Forward
                            bottomSheetView.findViewById<View>(R.id.forward).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoForward() -> {
                                        TabInfo.currentWebView().goForward()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Close Menu
                            bottomSheetView.findViewById<View>(R.id.close_menu).setOnClickListener {
                                bottomSheetDialog.dismiss()
                            }
//                    Reload
                            bottomSheetView.findViewById<View>(R.id.reload).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    web_view.url.startsWith("http") -> {
                                        reloadCurrentTab()
                                    }

                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Share
                            bottomSheetView.findViewById<View>(R.id.share).setOnClickListener {


                                val sharingUrl: String = web_view.url.toString()

                                if (web_view.url.startsWith("https://winkbr-browser.web.app")) {
                                } else if (web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/")) {
                                } else if (web_view.url.startsWith("https://doodlecricket.github.io/")) {
                                } else if (web_view.url.startsWith("https://hextris.io")) {
                                } else if (web_view.url.startsWith("https://nebezb.com/floppybird/")) {
                                } else if (web_view.url.startsWith("http")) {
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = "text/plain"
                                    intent.putExtra(Intent.EXTRA_TEXT, sharingUrl)
                                    startActivity(
                                        Intent.createChooser(
                                            intent,
                                            "Share with"
                                        )
                                    )
                                }

                                bottomSheetDialog.dismiss()
                            }

                            bottomSheetDialog.setContentView(bottomSheetView)
                            bottomSheetDialog.show()

                            return@setOnNavigationItemSelectedListener true
                        }
                        (web_view.url.toString().startsWith("https://nebezb.com/floppybird/")) -> {
                            val bottomSheetDialog = BottomSheetDialog(
                                this,
                                R.style.BottomSheetDialogTheme
                            )
                            val bottomSheetView = layoutInflater.inflate(
                                R.layout.layout_bottom_sheet_disabled, null
                            )

//                    Bookmarks
                            bottomSheetView.findViewById<View>(R.id.show_bookmarks)
                                .setOnClickListener {
                                    val intent = Intent(this, BookmarkActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    History
                            bottomSheetView.findViewById<View>(R.id.show_history)
                                .setOnClickListener {
                                    val intent = Intent(this, HistoryActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Downloads
                            bottomSheetView.findViewById<View>(R.id.show_downloads)
                                .setOnClickListener {
                                    val intent = Intent(this, DownloadActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Settings
                            bottomSheetView.findViewById<View>(R.id.settings).setOnClickListener {
                                val intent = Intent(this, SettingsActivity::class.java)
                                startActivity(intent)
                                bottomSheetDialog.dismiss()
                            }
//                    Add Tab
                            bottomSheetView.findViewById<View>(R.id.new_tab).setOnClickListener {
                                addTab()
                                bottomSheetDialog.dismiss()
                            }
//                    New Private Tab
                            bottomSheetView.findViewById<View>(R.id.new_private_tab).setOnClickListener {

                                addProxyTab()

                                bottomSheetDialog.dismiss()
                            }
//                    Add Bookmark
                            bottomSheetView.findViewById<View>(R.id.add_bookmark)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }


                                        web_view.url.startsWith("http") -> {
                                            val message: String
                                            val sharedPreferences = getSharedPreferences(
                                                PREFERENCES,
                                                MODE_PRIVATE
                                            )
                                            val jsonLink =
                                                sharedPreferences.getString(WEB_LINKS, null)
                                            val jsonTitle =
                                                sharedPreferences.getString(WEB_TITLE, null)
                                            message = if (jsonLink != null && jsonTitle != null) {
                                                val gson = Gson()
                                                val linkList = gson.fromJson<ArrayList<String>>(
                                                    jsonLink,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                val titleList = gson.fromJson<ArrayList<String>>(
                                                    jsonTitle,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                if (linkList.contains(web_view!!.url.toString())) {
                                                    "Bookmark exits"
                                                } else {
                                                    linkList.add(web_view!!.url.toString())
                                                    titleList.add(web_view!!.title.trim { it <= ' ' })
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString(
                                                        WEB_LINKS,
                                                        Gson().toJson(linkList)
                                                    )
                                                    editor.putString(
                                                        WEB_TITLE,
                                                        Gson().toJson(titleList)
                                                    )
                                                    editor.apply()
                                                    "Bookmark added"
                                                }
                                            } else {
                                                val linkList = ArrayList<String>()
                                                val titleList = ArrayList<String>()
                                                linkList.add(web_view!!.url.toString())
                                                titleList.add(web_view!!.title)
                                                val editor = sharedPreferences.edit()
                                                editor.putString(WEB_LINKS, Gson().toJson(linkList))
                                                editor.putString(
                                                    WEB_TITLE,
                                                    Gson().toJson(titleList)
                                                )
                                                editor.apply()
                                                "Bookmark added"
                                            }
                                            makeText(
                                                this@TabActivity,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            bottomSheetDialog.dismiss()
                                        }


                                    }

                                }

//                    Find in Page
                            bottomSheetView.findViewById<View>(R.id.find_in_page)
                                .setOnClickListener {
                                    val sharingUrl: String = web_view.url.toString()
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        else -> {
//                              find in page Dialog
                                            web_view.showFindDialog("", true)
                                            bottomSheetDialog.setContentView(bottomSheetView)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    View Source
                            bottomSheetView.findViewById<View>(R.id.page_source)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            web_view.settings.useWideViewPort = false
                                            val sourceMobileUrl: String = web_view.url.toString()
                                            web_view.loadUrl("view-source:$sourceMobileUrl")
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Print
                            bottomSheetView.findViewById<View>(R.id.print).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
//                                        bottomSheetDialog.dismiss()
                                    }
                                    web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                    }
                                    web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                    }
                                    web_view.url.startsWith("https://hextris.io") -> {
                                    }
                                    web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                    }

                                    web_view.url.startsWith("http") -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                    else -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                }
                            }
//                    Desktop Site
                            bottomSheetView.findViewById<View>(R.id.desktop_site)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        }
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }
                                        web_view.url.startsWith("http") -> {
                                            val sourceUrl: String = web_view.url.toString()

                                            web_view.settings.loadWithOverviewMode = true
                                            web_view.settings.useWideViewPort = true
                                            web_view.settings.setSupportZoom(true)
                                            web_view.settings.builtInZoomControls = true
                                            web_view.settings.displayZoomControls = false
//                                            web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
                                            web_view.isScrollbarFadingEnabled = false
                                            val newUA =
                                                "Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0"
                                            web_view.settings.userAgentString = newUA

                                            web_view.loadUrl(sourceUrl)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Feedback
                            bottomSheetView.findViewById<View>(R.id.feedback).setOnClickListener {
                                val emailIntent = Intent(
                                    Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", "support@winkbr.com", null
                                    )
                                )
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                                startActivity(Intent.createChooser(emailIntent, "Send email..."))
                                bottomSheetDialog.dismiss()
                                bottomSheetDialog.dismiss()
                            }
//                    Exit
                            bottomSheetView.findViewById<View>(R.id.exit).setOnClickListener {
                                val dialogClickListener =
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                TabInfo.removeAllTabs()
                                                finishAffinity()
                                            }
                                            DialogInterface.BUTTON_NEGATIVE -> {
                                            }
                                        }
                                    }
                                val builder =
                                    AlertDialog.Builder(this)
                                builder.setMessage("Are you sure?")
                                    .setPositiveButton(
                                        Html.fromHtml("<font color='#000000'>Yes</font>"),
                                        dialogClickListener
                                    )
                                    .setNegativeButton(
                                        Html.fromHtml("<font color='#000000'>No</font>"),
                                        dialogClickListener
                                    )
                                    .show()

                                bottomSheetDialog.dismiss()
                            }
//                    Back
                            bottomSheetView.findViewById<View>(R.id.back).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoBack() -> {
                                        TabInfo.currentWebView().goBack()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Forward
                            bottomSheetView.findViewById<View>(R.id.forward).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoForward() -> {
                                        TabInfo.currentWebView().goForward()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Close Menu
                            bottomSheetView.findViewById<View>(R.id.close_menu).setOnClickListener {
                                bottomSheetDialog.dismiss()
                            }
//                    Reload
                            bottomSheetView.findViewById<View>(R.id.reload).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    web_view.url.startsWith("http") -> {
                                        reloadCurrentTab()
                                    }

                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Share
                            bottomSheetView.findViewById<View>(R.id.share).setOnClickListener {


                                val sharingUrl: String = web_view.url.toString()

                                if (web_view.url.startsWith("https://winkbr-browser.web.app")) {
                                } else if (web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/")) {
                                } else if (web_view.url.startsWith("https://doodlecricket.github.io/")) {
                                } else if (web_view.url.startsWith("https://hextris.io")) {
                                } else if (web_view.url.startsWith("https://nebezb.com/floppybird/")) {
                                } else if (web_view.url.startsWith("http")) {
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = "text/plain"
                                    intent.putExtra(Intent.EXTRA_TEXT, sharingUrl)
                                    startActivity(
                                        Intent.createChooser(
                                            intent,
                                            "Share with"
                                        )
                                    )
                                }

                                bottomSheetDialog.dismiss()
                            }

                            bottomSheetDialog.setContentView(bottomSheetView)
                            bottomSheetDialog.show()

                            return@setOnNavigationItemSelectedListener true
                        }


                        (web_view.url.toString().startsWith("http")) -> {
                            val bottomSheetDialog = BottomSheetDialog(
                                this,
                                R.style.BottomSheetDialogTheme
                            )
                            val bottomSheetView = layoutInflater.inflate(
                                R.layout.layout_bottom_sheet, null
                            )
//                    Bookmarks
                            bottomSheetView.findViewById<View>(R.id.show_bookmarks)
                                .setOnClickListener {
                                    val intent = Intent(this, BookmarkActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    History
                            bottomSheetView.findViewById<View>(R.id.show_history)
                                .setOnClickListener {
                                    val intent = Intent(this, HistoryActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Downloads
                            bottomSheetView.findViewById<View>(R.id.show_downloads)
                                .setOnClickListener {
                                    val intent = Intent(this, DownloadActivity::class.java)
                                    startActivity(intent)
                                    bottomSheetDialog.dismiss()
                                }
//                    Settings
                            bottomSheetView.findViewById<View>(R.id.settings).setOnClickListener {
                                val intent = Intent(this, SettingsActivity::class.java)
                                startActivity(intent)
                                bottomSheetDialog.dismiss()
                            }
//                    Add Tab
                            bottomSheetView.findViewById<View>(R.id.new_tab).setOnClickListener {
                                addTab()
                                bottomSheetDialog.dismiss()
                            }
//                    New Private Tab
                            bottomSheetView.findViewById<View>(R.id.new_private_tab).setOnClickListener {

                                addProxyTab()

                                bottomSheetDialog.dismiss()
                            }
//                    Add Bookmark
                            bottomSheetView.findViewById<View>(R.id.add_bookmark)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/") -> {
                                        }
                                        web_view.url.startsWith("https://doodlecricket.github.io/") -> {
                                        }
                                        web_view.url.startsWith("https://hextris.io") -> {
                                        }
                                        web_view.url.startsWith("https://nebezb.com/floppybird/") -> {
                                        }

                                        web_view.url.startsWith("http") -> {
                                            val message: String
                                            val sharedPreferences = getSharedPreferences(
                                                PREFERENCES,
                                                MODE_PRIVATE
                                            )
                                            val jsonLink =
                                                sharedPreferences.getString(WEB_LINKS, null)
                                            val jsonTitle =
                                                sharedPreferences.getString(WEB_TITLE, null)
                                            message = if (jsonLink != null && jsonTitle != null) {
                                                val gson = Gson()
                                                val linkList = gson.fromJson<ArrayList<String>>(
                                                    jsonLink,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                val titleList = gson.fromJson<ArrayList<String>>(
                                                    jsonTitle,
                                                    object :
                                                        TypeToken<ArrayList<String?>?>() {}.type
                                                )
                                                if (linkList.contains(web_view!!.url.toString())) {
                                                    "Bookmark exits"
                                                } else {
                                                    linkList.add(web_view!!.url.toString())
                                                    titleList.add(web_view!!.title.trim { it <= ' ' })
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString(
                                                        WEB_LINKS,
                                                        Gson().toJson(linkList)
                                                    )
                                                    editor.putString(
                                                        WEB_TITLE,
                                                        Gson().toJson(titleList)
                                                    )
                                                    editor.apply()
                                                    "Bookmark added"
                                                }
                                            } else {
                                                val linkList = ArrayList<String>()
                                                val titleList = ArrayList<String>()
                                                linkList.add(web_view!!.url.toString())
                                                titleList.add(web_view!!.title)
                                                val editor = sharedPreferences.edit()
                                                editor.putString(WEB_LINKS, Gson().toJson(linkList))
                                                editor.putString(
                                                    WEB_TITLE,
                                                    Gson().toJson(titleList)
                                                )
                                                editor.apply()
                                                "Bookmark added"
                                            }
                                            makeText(
                                                this@TabActivity,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            bottomSheetDialog.dismiss()
                                        }

                                    }

                                }

//                    Find in Page
                            bottomSheetView.findViewById<View>(R.id.find_in_page)
                                .setOnClickListener {
                                    val sharingUrl: String = web_view.url.toString()
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                            makeText(
                                                this@TabActivity,
                                                "Homepage",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                        else -> {
//                              find in page Dialog
                                            web_view.showFindDialog("", true)
                                            bottomSheetDialog.setContentView(bottomSheetView)
                                            bottomSheetDialog.dismiss()

                                        }
                                    }
                                }
//                    View Source
                            bottomSheetView.findViewById<View>(R.id.page_source)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                            makeText(
                                                this@TabActivity,
                                                "Homepage",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                        web_view.url.startsWith("http") -> {
                                            web_view.settings.useWideViewPort = false
                                            val sourceMobileUrl: String = web_view.url.toString()
                                            web_view.loadUrl("view-source:$sourceMobileUrl")
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Print
                            bottomSheetView.findViewById<View>(R.id.print).setOnClickListener {
                                when {

                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        bottomSheetDialog.dismiss()
                                    }
                                    web_view.url.startsWith("http") -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                    else -> {
                                        val printManager =
                                            this.getSystemService(PRINT_SERVICE) as PrintManager
                                        val printAdapter = web_view!!.createPrintDocumentAdapter()
                                        val jobName = web_view!!.title.toString()
                                        printManager.print(
                                            jobName,
                                            printAdapter,
                                            PrintAttributes.Builder().build()
                                        )
                                        bottomSheetDialog.dismiss()
                                    }
                                }
                            }
//                    Desktop Site
                            bottomSheetView.findViewById<View>(R.id.desktop_site)
                                .setOnClickListener {
                                    when {
                                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                            makeText(
                                                this@TabActivity,
                                                "Homepage",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                        web_view.url.startsWith("http") -> {
                                            val sourceUrl: String = web_view.url.toString()

                                            web_view.settings.loadWithOverviewMode = true
                                            web_view.settings.useWideViewPort = true
                                            web_view.settings.setSupportZoom(true)
                                            web_view.settings.builtInZoomControls = true
                                            web_view.settings.displayZoomControls = false
//                                            web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
                                            web_view.isScrollbarFadingEnabled = false
                                            val newUA =
                                                "Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0"
                                            web_view.settings.userAgentString = newUA

                                            web_view.loadUrl(sourceUrl)
                                            bottomSheetDialog.dismiss()
                                        }
                                    }
                                }
//                    Feedback
                            bottomSheetView.findViewById<View>(R.id.feedback).setOnClickListener {
                                val emailIntent = Intent(
                                    Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", "support@winkbr.com", null
                                    )
                                )
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                                startActivity(Intent.createChooser(emailIntent, "Send email..."))
                                bottomSheetDialog.dismiss()
                            }
//                    Exit
                            bottomSheetView.findViewById<View>(R.id.exit).setOnClickListener {
                                val dialogClickListener =
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                TabInfo.removeAllTabs()
                                                finishAffinity()
                                            }
                                            DialogInterface.BUTTON_NEGATIVE -> {
                                            }

                                        }
                                    }
                                val builder =
                                    AlertDialog.Builder(this)
                                builder.setMessage("Are you sure?")
                                    .setPositiveButton(
                                        Html.fromHtml("<font color='#000000'>Yes</font>"),
                                        dialogClickListener
                                    )
                                    .setNegativeButton(
                                        Html.fromHtml("<font color='#000000'>No</font>"),
                                        dialogClickListener
                                    )
                                    .show()

                                bottomSheetDialog.dismiss()
                            }
//                    Back
                            bottomSheetView.findViewById<View>(R.id.back).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoBack() -> {
                                        TabInfo.currentWebView().goBack()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Forward
                            bottomSheetView.findViewById<View>(R.id.forward).setOnClickListener {
                                when {
                                    TabInfo.currentWebView().canGoForward() -> {
                                        TabInfo.currentWebView().goForward()
                                    }
                                    else -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }

                                bottomSheetDialog.dismiss()
                            }
//                    Close Menu
                            bottomSheetView.findViewById<View>(R.id.close_menu).setOnClickListener {
                                bottomSheetDialog.dismiss()
                            }
//                    Reload
                            bottomSheetView.findViewById<View>(R.id.reload).setOnClickListener {
                                when {
                                    web_view.url.startsWith("https://winkbr-browser.web.app") -> {
                                        makeText(
                                            this@TabActivity,
                                            "Homepage",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    web_view.url.startsWith("http") -> {
                                        reloadCurrentTab()
                                    }

                                }
                                bottomSheetDialog.dismiss()
                            }
//                    Share
                            bottomSheetView.findViewById<View>(R.id.share).setOnClickListener {


                                val sharingUrl: String = web_view.url.toString()
                                if (web_view.url.startsWith("https://winkbr-browser.web.app")) {
                                } else if (web_view.url.startsWith("http://13.127.225.49/")) {
                                } else if (web_view.url.startsWith("https://hczhcz.github.io/2048/20ez/")) {
                                } else if (web_view.url.startsWith("https://doodlecricket.github.io/")) {
                                } else if (web_view.url.startsWith("https://hextris.io")) {
                                } else if (web_view.url.startsWith("https://nebezb.com/floppybird/")) {
                                } else {
                                    try {
                                        val intent = Intent(Intent.ACTION_SEND)
                                        intent.type = "text/plain"
                                        intent.putExtra(Intent.EXTRA_TEXT, sharingUrl)
                                        startActivity(
                                            Intent.createChooser(
                                                intent,
                                                "Share with"
                                            )
                                        )
                                    } catch (e: Exception) {
                                        makeText(
                                            this@TabActivity,
                                            "Install apps to share",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }



                                bottomSheetDialog.dismiss()
                            }

                            bottomSheetDialog.setContentView(bottomSheetView)
                            bottomSheetDialog.show()

                            return@setOnNavigationItemSelectedListener true
                        }
                        else -> {
                            makeText(
                                this@TabActivity,
                                "neither file nor http",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return@setOnNavigationItemSelectedListener true
                        }
                    }

                }
                R.id.nav_news -> {
//                    R.id.nav_reload -> {
//                    when {
//                        web_view.url.startsWith("https://winkbr-browser.web.app") -> {
//                            makeText(
//                                this@TabActivity,
//                                "Homepage",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        }
//                        web_view.url.startsWith("http") -> {
//                            reloadCurrentTab()
//                        }
//
//                    }

                    val intent = Intent(this, NewsActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_settings -> {
//                    settt
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }

        var address: AutoCompleteTextView? = null
        address = findViewById<View>(R.id.address_bar) as AutoCompleteTextView


        address.setOnFocusChangeListener { view, b ->
            run {
                if (view.hasFocus()) {
                    collapseToolbar()
                } else if (!view.hasFocus()) {
                    hideKeyboard(view)
                } else {
                    makeText(this, "", Toast.LENGTH_SHORT).show()
                }

            }
        }

        address.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                view.clearFocus()
                val query = view.text.toString()
                TabInfo.currentWebView().search(query)

                true
            } else {
                false
            }
        }

        if (savedInstanceState != null) TabInfo.currentIndex =
            savedInstanceState.getInt("current_index")
    }

    override fun onResume() {
        super.onResume()
        refreshTabs()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun reloadCurrentTab() {
        val webView = TabInfo.currentWebView()
        webView.reload()
    }

    fun addTab() {
        val tab = Tab(this)
        tab.web_view.loadHome()
        TabInfo.addTab(tab)
        refreshTabs()
    }

    fun addProxyTab() {
        val tab = Tab(this)
        tab.web_view.loadUrl("http://13.127.225.49/proxy")
        TabInfo.addTab(tab)
        refreshTabs()
    }

    private fun refreshTabs() {
        if (TabInfo.currentIndex == -1) {
            addTab()
        } else {
            val tab = TabInfo.currentTab()
            if (tab.parent != null) {
                val vg = tab.parent as ViewGroup
                vg.removeView(tab)
            }

            frame_layout.removeAllViews()
            frame_layout.addView(tab)

            countButton?.text = TabInfo.count().toString()

            refreshForwardButton()
        }
    }

    private fun refreshForwardButton() {
        if (TabInfo.currentWebView().canGoForward()) {
            forwardButton?.isEnabled = true
            forwardButton?.icon?.alpha = 255
        } else {
            forwardButton?.isEnabled = false
            forwardButton?.icon?.alpha = 25
        }
    }

    override fun onBackPressed() {
        val webView = TabInfo.currentWebView()

//        expandAppBarLayout(true)


        if (webView.canGoBack()) {
            webView.goBack()
            refreshForwardButton()
        } else {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            TabInfo.removeAllTabs()
                            finishAffinity()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }

                    }
                }
            val builder =
                AlertDialog.Builder(this)
            builder.setMessage("Are you sure?")
                .setPositiveButton(
                    Html.fromHtml("<font color='#000000'>Yes</font>"),
                    dialogClickListener
                )
                .setNegativeButton(
                    Html.fromHtml("<font color='#000000'>No</font>"),
                    dialogClickListener
                )
                .show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tab_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        forwardButton = menu?.findItem(R.id.nav_forward)
        refreshForwardButton()
        return super.onPrepareOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.tab_list_button -> {
                val intent = Intent(this, TabListActivity::class.java)
                startActivity(intent)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is AutoCompleteTextView) {
                val rect = Rect()
                view.getGlobalVisibleRect(rect)
                if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    view.clearFocus()
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt("current_index", TabInfo.currentIndex)

        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        fun ep() {

        }

        const val PREFERENCES = "PREFERENCES_NAME"
        const val WEB_LINKS = "links"
        const val WEB_TITLE = "title"
        private const val MY_PERMISSION_REQUEST_CODE = 123
    }

    private fun loadImg() {
        val bgImg = "http://source.unsplash.com/random/320x180"
        var imgView: ImageView? = null
        imgView = findViewById(R.id.backgroundImg)
        Glide.with(this)
            .load(bgImg)
            .placeholder(R.drawable.temporary)
            .error(R.drawable.temporary)
            .fitCenter()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(imgView)
//        Picasso
//            .get()
//            .load(bgImg)
//            .fit()
//            .centerCrop()
//            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//            .into(imgView)
    }

    private fun tabCountClick(){
        val intent = Intent(this, TabListActivity::class.java)
        startActivity(intent)
    }

    fun collapseToolbar(){
        val tabIco: TextView = findViewById(R.id.tabCount)
        tabIco.visibility = View.VISIBLE
        val statusIco: ImageView = findViewById(R.id.statusIcon)
        statusIco.visibility = View.VISIBLE

        appBarLayout = findViewById<View>(R.id.appBarLayout) as AppBarLayout?
        appBarLayout?.isActivated = true
        appBarLayout?.setExpanded(false, true)
    }


}

