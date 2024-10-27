package com.tps.challenge.data.source.network

import com.tps.challenge.data.model.User
import com.tps.challenge.data.model.UserProfile

interface GitHubRepository {
    suspend fun fetchAllUser(): List<User>
    suspend fun fetchUser(userName: String): UserProfile
}

class GithubRepositoryImpl(private val apiService: GitHubServiceApi) : GitHubRepository {
    override suspend fun fetchAllUser() = apiService.getAllUsers()
    override suspend fun fetchUser(userName: String) = apiService.getUserProfile(userName)
}