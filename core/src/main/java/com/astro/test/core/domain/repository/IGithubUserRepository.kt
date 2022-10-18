package com.astro.test.core.domain.repository

import androidx.paging.PagingData
import com.astro.test.core.data.Resource
import com.astro.test.core.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface IGithubUserRepository {

    fun searchUsers(query: String, sort: String, order: String): Flow<PagingData<GithubUser>>

    fun getFavoriteUsers(): Flow<List<GithubUser>>

    suspend fun setFavoriteUser(user: GithubUser)

    suspend fun deleteFavorite(userId: Int)
}