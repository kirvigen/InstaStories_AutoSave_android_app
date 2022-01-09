package com.kirvigen.instagram.stories.autosave.instagramUtils.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stories(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "sourceMedia")
    val sourceMedia: String,
    @ColumnInfo(name = "userId")
    val userId: String
)
