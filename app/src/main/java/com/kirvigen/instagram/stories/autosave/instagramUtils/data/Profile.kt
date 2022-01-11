package com.kirvigen.instagram.stories.autosave.instagramUtils.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Parceli
data class Profile(
    @PrimaryKey
    var id: Long,
    val photo: String,
    val name: String,
    val description: String = "",
    val isCurrentProfile: Boolean = false
)
