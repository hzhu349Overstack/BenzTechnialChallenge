package com.tps.challenge.data.source.network

import com.tps.challenge.data.model.User
import com.tps.challenge.data.model.UserProfile
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubServiceApi {
    @GET("/users")
    suspend fun getAllUsers(): List<User>

    @GET("/users/{USER_ID}")
    suspend fun getUserProfile(@Path("USER_ID")userId: String ): UserProfile
}