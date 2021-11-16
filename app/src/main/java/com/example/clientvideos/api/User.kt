package com.example.clientvideos.api

import com.beust.klaxon.Json
import com.estimote.coresdk.repackaged.gson_v2_3_1.com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class User(
    val id:Int,
    val login:String,
    val password :String
          )
