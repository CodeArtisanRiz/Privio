package com.winkbr.browser

import androidx.room.*
import com.winkbr.browser.classes.Bookmark

@Dao
interface BookmarkDao {
    @Query("SELECT * from bookmark ORDER BY title")
    fun getAll(): List<Bookmark>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmark: Bookmark)

    @Delete
    fun delete(bookmark: Bookmark)
}