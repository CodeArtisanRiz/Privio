package com.winkbr.browser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.winkbr.browser.R
import androidx.cardview.widget.CardView



class CreditsActivity : AppCompatActivity() {

    private var c1: CardView?=null
    private var c2: CardView?=null
    private var c3: CardView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)
        supportActionBar?.title = "Credits"
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        this.c1 = findViewById(R.id.c1)
//        this.c1!!.setOnClickListener {
//            val intent = Intent(this@CreditsActivity, CreditDetailsActivity::class.java)
//            intent.putExtra("name", "Freepik")
//            intent.putExtra("license", "Icons made by Freepik from www.flaticon.com is licensed by CC 3.0. Visit them at:\nhttps://www.freepik.com")
//            startActivity(intent)
//        }
//        this.c2 = findViewById(R.id.c2)
//        this.c2!!.setOnClickListener {
//            val intent = Intent(this@CreditsActivity, CreditDetailsActivity::class.java)
//            intent.putExtra("name", "Drone Shot")
//            intent.putExtra("license", "Picture shot by Drone Photography, Silchar\nVisit them:\nhttps://wwww.facebook.com/dronnephotographysilchar/")
//            startActivity(intent)
//        }
    }
}