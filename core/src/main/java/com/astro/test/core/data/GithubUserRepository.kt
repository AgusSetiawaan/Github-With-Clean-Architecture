package com.astro.test.core.data

import com.astro.test.core.data.source.local.LocalDataSource
import com.astro.test.core.data.source.remote.RemoteDataSource
import com.astro.test.core.domain.model.GithubUser
import com.astro.test.core.domain.repository.IGithubUserRepository
import com.astro.test.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GithubUserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource)
    : IGithubUserRepository {
    override fun searchUsers(
        query: String,
        sort: String,
        order: String
    ): Flow<Resource<List<GithubUser>>> {

    }

    override fun getFavoriteUsers(): Flow<List<GithubUser>> =
        localDataSource.getFavoriteUsers().map {
            DataMapper.mapEntitiesToDomain(it)
        }

    override suspend fun setFavoriteUser(user: GithubUser) =
        localDataSource.setFavoriteUser(DataMapper.mapDomainToEntity(user))


    override suspend fun deleteFavorite(userId: Int) =
        localDataSource.deleteFavorite(userId)
}