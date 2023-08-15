package com.t3g.privio.classes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.t3g.privio.R
import com.t3g.privio.activity.TabActivity
import kotlinx.android.synthetic.main.view_tab.view.*

class Tab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_tab, this)
        web_view.activity = context as TabActivity
        web_view.progressBar = progress_bar
    }
}