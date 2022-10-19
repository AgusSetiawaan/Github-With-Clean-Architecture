package com.astro.test.agussetiawan.core.data.source.local

import com.astro.test.agussetiawan.core.data.source.local.entity.GithubUserEntity
import com.astro.test.agussetiawan.core.data.source.local.room.GithubUserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: GithubUserDao) {

    suspend fun setFavoriteUser(user: GithubUserEntity) = dao.setFavoriteUser(user)

    suspend fun deleteFavorite(userId: Int) = dao.deleteFavorite(userId)

    fun getFavoriteUsers(): Flow<List<GithubUserEntity>> = dao.getFavoriteUsers()
}