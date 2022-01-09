package com.kirvigen.instagram.stories.autosave.instagramUtils.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "photo")
    val photo: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "isCorrect")
    val isCorrentProfile: Boolean = false
)
