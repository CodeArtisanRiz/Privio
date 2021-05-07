package com.winkbr.browser.activity

import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.winkbr.browser.Database
import com.winkbr.browser.R
import com.winkbr.browser.adapter.HistoryAdapter
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    lateinit var viewAdapter: HistoryAdapter
    lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter()
        viewAdapter.activity = this
        tab_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.remove_all -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Clear all History?")
                builder.setMessage("This cannot be undone")
                builder.setPositiveButton("Yes") { _, _ ->
                    AsyncTask.execute {
                        Database.db?.historyDao()?.nukeTable()
                    }

                    finish()
                }

                builder.setNegativeButton("No") { _, _ -> {}}

                val dialog = builder.create()
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
