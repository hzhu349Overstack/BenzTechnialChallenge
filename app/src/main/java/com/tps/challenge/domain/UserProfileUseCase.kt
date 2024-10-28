package com.tps.challenge.domain

import com.tps.challenge.data.source.network.GitHubRepository

class UserProfileUseCase(private val repository: GitHubRepository) {
    suspend fun invoke(username: String) =
        repository.fetchUser(username)
}