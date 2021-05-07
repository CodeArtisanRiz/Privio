package com.winkbr.browser

import android.content.Context
import androidx.room.Room

object Database {
    var db: AppDatabase? = null

    fun initDb(context: Context) {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, "db").build()
        }
    }
}