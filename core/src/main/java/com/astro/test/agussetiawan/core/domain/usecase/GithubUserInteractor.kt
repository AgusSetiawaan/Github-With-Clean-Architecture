package com.astro.test.agussetiawan.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.astro.test.agussetiawan.core.domain.model.DataItem
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.core.domain.model.SortType
import com.astro.test.agussetiawan.core.domain.repository.IGithubUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GithubUserInteractor @Inject constructor(private val repository: IGithubUserRepository): GithubUserUseCase {
    override fun searchUsers(
        query: String,
        sort: String?,
        sortType: SortType
    ): Flow<PagingData<DataItem>> {
        return repository.searchUsers(query, sort, sortType.desc).map { pagingData ->
            pagingData.map {
                DataItem.UserItem(it) as DataItem
            }.insertHeaderItem(item = DataItem.Header(sortType))
        }
    }

    override fun getFavoriteUsers(): Flow<List<GithubUser>> {
        return repository.getFavoriteUsers()
    }

    override suspend fun setFavoriteUser(user: GithubUser) {
        return repository.setFavoriteUser(user)
    }

    override suspend fun deleteFavorite(userId: Int) {
        return repository.deleteFavorite(userId)
    }

    override fun getSortType(): Flow<SortType> {
        return repository.getSortType()
    }

    override suspend fun setSortType(sortType: SortType) {
        return repository.saveSortType(sortType)
    }
}