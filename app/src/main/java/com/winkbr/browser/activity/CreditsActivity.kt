package com.winkbr.browser.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.winkbr.browser.R


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
        this.c2 = findViewById(R.id.c2)
        this.c2!!.setOnClickListener {
            Toast.makeText(applicationContext,"Loading...",Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_MAIN).addCategory("CATEGORY_APP_BROWSER")
//            intent.addCategory(Intent.)
            startActivity(Intent.createChooser(intent, "Please set launcher settings to ALWAYS"));
        }
    }
}