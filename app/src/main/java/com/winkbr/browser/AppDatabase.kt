package com.winkbr.browser

import androidx.room.Database
import androidx.room.RoomDatabase
import com.winkbr.browser.classes.Bookmark
import com.winkbr.browser.classes.History
import com.winkbr.browser.adapter.HistoryDao

@Database(entities = [Bookmark::class, History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun historyDao(): HistoryDao
}