package com.example.bfaa.data.model

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(
    val login: String,
    val id: Int,
    // The API return avatar_url but we want to use avatarUrl as variable
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("followers_url")
    val followersUrl: String,
    @SerializedName("following_url")
    val followingUrl: String,
    val name: String,
    val following: Int,
    val followers: Int
)
