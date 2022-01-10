package com.kirvigen.instagram.stories.autosave.instagramUtils.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey()
    var id: Long,
    val photo: String,
    val name: String,
    val description: String = "",
    val isCorrectProfile: Boolean = false
)
