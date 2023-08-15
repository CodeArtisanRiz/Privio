package com.t3g.privio.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.t3g.privio.R
import com.t3g.privio.adapter.CategoryAdapter

class NewsActivity : AppCompatActivity() {
    private var mContext: Context? = null
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        mContext = this

        // Find the view pager that will allow the user to swipe between fragments
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        // Create an adapter that knows which fragment should be shown on each page
        val adapter = CategoryAdapter(mContext, supportFragmentManager)

        // Set the adapter onto the view pager
        viewPager.adapter = adapter

        // Find the tab layout that shows the tabs
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        Inflate the options menu from XML
//        menuInflater.inflate(R.menu.op_main_menu, menu)

        return true
    }

}