package com.astro.test.core.domain.repository

import com.astro.test.core.data.Resource
import com.astro.test.core.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface IGithubUserRepository {

    fun searchUsers(query: String, sort: String, order: String): Flow<Resource<List<GithubUser>>>

    fun getFavoriteUsers(): Flow<List<GithubUser>>

    suspend fun setFavoriteUser(user: GithubUser)

    suspend fun deleteFavorite(userId: Int)
}