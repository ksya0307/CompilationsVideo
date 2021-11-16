package com.example.clientvideos.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Video(
    @SerializedName("id")
    val Id:String,

    @SerializedName("name")
    val name:String,

    @SerializedName("preview")
    val preview:String
    )
