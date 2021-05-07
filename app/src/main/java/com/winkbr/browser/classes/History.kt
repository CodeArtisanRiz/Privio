package com.winkbr.browser.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History (
    @PrimaryKey (autoGenerate = true) val uid: Int?,
    @ColumnInfo (name = "title") val title: String,
    @ColumnInfo (name = "address") val address: String,
    @ColumnInfo (name = "time") val time: Long
)