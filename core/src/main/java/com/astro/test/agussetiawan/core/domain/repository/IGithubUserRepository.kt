package com.astro.test.agussetiawan.core.domain.repository

import androidx.paging.PagingData
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.core.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface IGithubUserRepository {

    fun searchUsers(query: String, sort: String?, order: String?): Flow<PagingData<GithubUser>>

    fun getFavoriteUsers(): Flow<List<GithubUser>>

    suspend fun setFavoriteUser(user: GithubUser)

    suspend fun deleteFavorite(userId: Int)

    fun getSortType(): Flow<SortType>

    suspend fun saveSortType(sortType: SortType)
}