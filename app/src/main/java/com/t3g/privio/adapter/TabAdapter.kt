package com.t3g.privio.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.t3g.privio.R
import com.google.android.material.card.MaterialCardView
import com.t3g.privio.activity.TabActivity
import com.t3g.privio.activity.TabListActivity
import com.t3g.privio.tabs.TabInfo
import kotlinx.android.synthetic.main.activity_tab.*
import kotlinx.android.synthetic.main.view_tab_adapter.view.*

class TabAdapter(private val context: Context?) : RecyclerView.Adapter<TabAdapter.TabViewHolder>() {
    lateinit var activity: TabListActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_tab_adapter, parent,false)
        return TabViewHolder(view)
    }

    override fun getItemCount() = TabInfo.count()

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        val webView = TabInfo.webView(position)


        when {
            webView.url!!.startsWith(context!!.getString(R.string.homePage)) -> {
                holder.view.favicon.setImageBitmap(webView.favicon)
                holder.view.title.text = "Privio - Home"
                holder.view.address.text = "Home"
            }


            webView.url!!.startsWith("http://13.127.225.49/") -> {
                holder.view.favicon.setImageBitmap(webView.favicon)
                holder.view.title.text = "VPN Tab"
                holder.view.address.text = ""
            }
            else -> {
                holder.view.favicon.setImageBitmap(webView.favicon)
                holder.view.title.text = webView.title
                holder.view.address.text = webView.url
            }
        }

        val card = holder.view as MaterialCardView
        if (position == TabInfo.currentIndex) {
            card.strokeColor = Color.BLUE
            card.strokeWidth = 2
        } else {
            card.strokeWidth = 0
        }

        holder.view.setOnClickListener {
            val index = holder.adapterPosition
            TabInfo.currentIndex = index

            val address: AutoCompleteTextView = TabInfo.activity.address_bar
            val topImageView : ImageView = TabInfo.activity.backgroundImg

            if (webView.url!!.startsWith("file")) {
                address.setText("")
                address.hint = "Search or enter URL"
                address.isEnabled = true
                topImageView.visibility = View.VISIBLE
            }
//            else if (webView.url!!.startsWith("https://winkrbr-home.web.app")) {
//                address.setText("")
//                address.hint = "Search or enter URL"
//                address.isEnabled = true
//                topImageView.visibility = View.VISIBLE
//            }
            else if (webView.url!!.startsWith("https://winkbr-browser.web.app")) {
                address.setText("")
                address.hint = "Search or enter URL"
                address.isEnabled = true
                topImageView.visibility = View.VISIBLE
            }
            else if(webView.url!!.startsWith("http://13.127.225.49/")) {
                address.setText("")
                address.hint = "VPN Mode"
                address.isEnabled = false
                topImageView.visibility = View.GONE
            }
            else if(webView.url!!.startsWith("https://hczhcz.github.io/2048/20ez/")) {
                address.setText("")
                address.hint = "Game Mode"
                address.isEnabled = false
                topImageView.visibility = View.GONE

            }
            else if(webView.url!!.startsWith("https://doodlecricket.github.io/")) {
                address.setText("")
                address.hint = "Game Mode"
                address.isEnabled = false
                topImageView.visibility = View.GONE
            }
            else if(webView.url!!.startsWith("https://hextris.io")) {
                address.setText("")
                address.hint = "Game Mode"
                address.isEnabled = false
                topImageView.visibility = View.GONE
            }
            else if(webView.url!!.startsWith("https://nebezb.com/floppybird/")) {
                address.setText("")
                address.hint = "Game Mode"
                address.isEnabled = false
                topImageView.visibility = View.GONE
            }
            else if(webView.url!!.startsWith("https")) {
                address.setText(webView.url!!.toString())
                address.isEnabled = true
                topImageView.visibility = View.GONE
            }
            else if(webView.url!!.startsWith("http")) {
                address.setText(webView.url!!.toString())
                address.isEnabled = true
                topImageView.visibility = View.GONE
            }

            activity.finish()
        }

        holder.view.close_button.setOnClickListener {
            val index = holder.adapterPosition
            TabInfo.removeTab(index)
            notifyItemRemoved(index)
        }
    }

    class TabViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}