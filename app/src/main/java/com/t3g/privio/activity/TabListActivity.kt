package com.t3g.privio.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.t3g.privio.R
import com.t3g.privio.adapter.TabAdapter
import com.t3g.privio.tabs.TabInfo
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
        val tabAdapter = TabAdapter(applicationContext)
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
                val address: AutoCompleteTextView = TabInfo.activity.address_bar
                address.setText("")
                address.hint = "Search or enter URL"
                finish()
                return true
            }
            R.id.add_proxy_tab -> {
                TabInfo.activity.addProxyTab()
                val address: AutoCompleteTextView = TabInfo.activity.address_bar
                address.setText("")
                address.hint = "VPN Mode"
                address.isEnabled = false
                finish()
                return true
            }
            R.id.close_all -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Close all tabs")
                builder.setPositiveButton(Html.fromHtml("<font color='#000000'>Yes</font>")) { _, _ ->
                    TabInfo.removeAllTabs()
                    finish()
                }
                builder.setNegativeButton(Html.fromHtml("<font color='#000000'>No</font>")) { _, _ -> }
                val dialog = builder.create()
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
