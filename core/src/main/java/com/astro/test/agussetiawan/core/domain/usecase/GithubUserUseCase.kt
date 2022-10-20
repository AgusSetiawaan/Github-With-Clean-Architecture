package com.astro.test.agussetiawan.core.domain.usecase

import androidx.paging.PagingData
import com.astro.test.agussetiawan.core.domain.model.DataItem
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.core.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface GithubUserUseCase {

    fun searchUsers(query: String, sort: String?, sortType: SortType): Flow<PagingData<DataItem>>

    fun getFavoriteUsers(): Flow<List<GithubUser>>

    suspend fun setFavoriteUser(user: GithubUser)

    suspend fun deleteFavorite(userId: Int)

    fun getSortType(): Flow<SortType>

    suspend fun setSortType(sortType: SortType)
}