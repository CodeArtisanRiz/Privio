package com.winkbr.browser.adapter

import android.app.AlertDialog
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.winkbr.browser.Database
import com.winkbr.browser.R
import com.winkbr.browser.activity.DownloadActivity
import com.winkbr.browser.classes.Bookmark
import kotlinx.android.synthetic.main.view_sites_adapter.view.*
import kotlinx.android.synthetic.main.view_tab_adapter.view.address
import kotlinx.android.synthetic.main.view_tab_adapter.view.title

class DownloadAdapter : RecyclerView.Adapter<DownloadAdapter.ViewHolder>() {
    var bookmarks = mutableListOf<Bookmark>()
    lateinit var activity: DownloadActivity

    init {
        AsyncTask.execute {
            val items = Database.db?.bookmarkDao()?.getAll()
            if (items != null) for (item in items) {
                val index = bookmarks.size
                bookmarks.add(item)
                notifyItemInserted(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_download_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = bookmarks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bookmarks[position]
        holder.view.title.text = item.title
        holder.view.address.text = item.address
        holder.view.extra_info.text = item.info

        holder.view.setOnClickListener {


//            TabInfo.addTab(item.address)
//            activity.finish()
        }

        holder.view.remove.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Clear?")
            builder.setPositiveButton("Yes") { _, _ ->
                val index = holder.adapterPosition
                AsyncTask.execute {
                    Database.db?.bookmarkDao()?.delete(item)
                }
                bookmarks.removeAt(index)
                notifyItemRemoved(index)
            }
            builder.setNegativeButton("No") { _, _ -> }
            val dialog = builder.create()
            dialog.show()
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}