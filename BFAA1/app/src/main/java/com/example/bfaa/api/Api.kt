package com.example.bfaa.api

import com.example.bfaa.data.model.DetailUserResponse
import com.example.bfaa.data.model.User
import com.example.bfaa.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}