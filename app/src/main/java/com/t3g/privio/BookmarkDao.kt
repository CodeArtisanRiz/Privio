package com.t3g.privio

import androidx.room.*
import com.t3g.privio.classes.Bookmark

@Dao
interface BookmarkDao {
    @Query("SELECT * from bookmark ORDER BY title")
    fun getAll(): List<Bookmark>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmark: Bookmark)

    @Delete
    fun delete(bookmark: Bookmark)
}