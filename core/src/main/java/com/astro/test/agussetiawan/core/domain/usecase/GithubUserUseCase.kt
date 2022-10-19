package com.astro.test.agussetiawan.core.domain.usecase

import androidx.paging.PagingData
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserUseCase {

    fun searchUsers(query: String, sort: String?, order: String?): Flow<PagingData<GithubUser>>

    fun getFavoriteUsers(): Flow<List<GithubUser>>

    suspend fun setFavoriteUser(user: GithubUser)

    suspend fun deleteFavorite(userId: Int)
}