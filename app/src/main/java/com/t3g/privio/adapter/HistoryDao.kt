package com.t3g.privio.adapter

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.t3g.privio.classes.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY time DESC")
    fun getAll(): List<History>

    @Insert
    fun insert(history: History)

    @Delete
    fun delete(history: History)

    @Query("DELETE FROM history")
    fun nukeTable()
}