package com.tps.challenge.domain

import com.tps.challenge.data.source.network.GitHubRepository

class FetchAllUserUseCase(private val userRepository: GitHubRepository) {
    suspend fun invoke() = userRepository.fetchAllUser()
}