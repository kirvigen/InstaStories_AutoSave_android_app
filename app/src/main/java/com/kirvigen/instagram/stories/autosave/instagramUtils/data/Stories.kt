package com.kirvigen.instagram.stories.autosave.instagramUtils.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Stories(
    @PrimaryKey
    var id: Long,
    val sourceMedia: String,
    val isVideo: Boolean,
    val userId: Long,
    val date: Long,
    val preview: String,
    val localUri: String = ""
):Parcelable
