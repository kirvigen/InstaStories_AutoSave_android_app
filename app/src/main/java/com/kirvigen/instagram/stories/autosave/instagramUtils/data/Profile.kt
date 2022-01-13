package com.kirvigen.instagram.stories.autosave.instagramUtils.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Profile(
    @PrimaryKey
    var id: Long,
    val photo: String,
    val name: String,
    val description: String = "",
    val nickname: String,
    val isCurrentProfile: Boolean = false
) : Parcelable
