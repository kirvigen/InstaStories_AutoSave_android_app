package com.kirvigen.instagram.stories.autosave.instagramUtils.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stories(
    @PrimaryKey
    var id: Long,
    val sourceMedia: String,
    val isVideo: Boolean,
    val userId: Long,
    val date: Long,
    val preview: String
)
