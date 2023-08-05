package com.example.bfaa.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val login: String,
    val id: Int,
    // The API return avatar_url but we want to use avatarUrl as variable
    @SerializedName("avatar_url")
    val avatarUrl: String
)
