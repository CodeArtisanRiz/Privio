package com.t3g.privio.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.t3g.privio.R
import com.t3g.privio.adapter.DownloadAdapter
import kotlinx.android.synthetic.main.activity_download.*

class DownloadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Downloads"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val linearLayoutManager = LinearLayoutManager(this)
        val viewAdapter = DownloadAdapter()
        viewAdapter.activity = this
        recycler_view.apply {
            layoutManager = linearLayoutManager
            adapter = viewAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
