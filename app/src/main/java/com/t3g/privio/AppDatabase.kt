package com.t3g.privio

import androidx.room.Database
import androidx.room.RoomDatabase
import com.t3g.privio.classes.Bookmark
import com.t3g.privio.classes.History
import com.t3g.privio.adapter.HistoryDao

@Database(entities = [Bookmark::class, History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun historyDao(): HistoryDao
}