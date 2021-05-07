package com.winkbr.browser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.winkbr.browser.R
import com.winkbr.browser.adapter.TabAdapter
import com.winkbr.browser.tabs.TabInfo
import kotlinx.android.synthetic.main.activity_tab.*
import kotlinx.android.synthetic.main.activity_tab_list.*
import kotlinx.android.synthetic.main.activity_tab_list.toolbar

class TabListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_list)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Tabs"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val linearLayoutManager = LinearLayoutManager(this)
        val tabAdapter = TabAdapter()
        tabAdapter.activity = this
        recycler_view.apply {
            adapter = tabAdapter
            layoutManager = linearLayoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tab_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.add_tab -> {
                TabInfo.activity.addTab()
                val address: AutoCompleteTextView = (TabInfo.activity as TabActivity).address_bar
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
                finish()
                return true
            }
            R.id.add_proxy_tab -> {
                TabInfo.activity.addProxyTab()
                val address: AutoCompleteTextView = (TabInfo.activity as TabActivity).address_bar
                address.setText("")
                address.hint = "VPN Mode"
                address.isEnabled = false
                val sslIco: ImageView = (TabInfo.activity as TabActivity).sslIcon
                sslIco.visibility = View.GONE
                val noSslIco: ImageView = (TabInfo.activity as TabActivity).noSslIcon
                noSslIco.visibility = View.GONE
                val homeIco: ImageView = (TabInfo.activity as TabActivity).homeIcon
                homeIco.visibility = View.GONE
                val proxyIco: ImageView = (TabInfo.activity as TabActivity).proxyIcon
                proxyIco.visibility = View.VISIBLE
                finish()
                return true
            }
            R.id.close_all -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Close all tabs")
                builder.setPositiveButton("Yes") { _, _ ->
                    TabInfo.removeAllTabs()
                    finish()
                }
                builder.setNegativeButton("No") { _, _ -> }
                val dialog = builder.create()
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
