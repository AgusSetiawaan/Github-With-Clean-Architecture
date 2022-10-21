package com.astro.test.agussetiawan.core.data.source.local

import com.astro.test.agussetiawan.core.data.source.local.datapreference.SortDataPreference
import com.astro.test.agussetiawan.core.data.source.local.entity.GithubUserEntity
import com.astro.test.agussetiawan.core.data.source.local.room.GithubUserDao
import com.astro.test.agussetiawan.core.domain.model.SortType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: GithubUserDao,
                                          private val sortDataPreference: SortDataPreference) {

    suspend fun setFavoriteUser(user: GithubUserEntity) = dao.setFavoriteUser(user)

    suspend fun deleteFavorite(userId: Int) = dao.deleteFavorite(userId)

    fun getFavoriteUsers(): Flow<List<GithubUserEntity>> = dao.getFavoriteUsers()

    fun getSortType(): Flow<SortType> {
        return sortDataPreference.getSortType()
    }

    suspend fun saveSortType(sortType: SortType) = sortDataPreference.saveSortType(sortType)
}