package com.tps.challenge.application

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.tps.challenge.data.source.network.GitHubServiceApi
import com.tps.challenge.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.tps.challenge.data.source.network.GithubRepositoryImpl
import com.tps.challenge.data.source.network.GitHubRepository
import com.tps.challenge.domain.FetchAllUserUseCase

interface AppComponent {
    val useCase: FetchAllUserUseCase
}

class DefaultAppComponent: AppComponent{
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: GitHubServiceApi by lazy {
        retrofit.create(GitHubServiceApi::class.java)
    }

    private val gitHubRepository: GitHubRepository by lazy {
        GithubRepositoryImpl(retrofitService)
    }

    override val useCase: FetchAllUserUseCase
        get() = FetchAllUserUseCase(gitHubRepository)

}