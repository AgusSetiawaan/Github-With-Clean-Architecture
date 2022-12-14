package com.agussetiawan.application.githubcleanarchitecture.core.data

import androidx.paging.*
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.GithubUserRemoteMediator
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.LocalDataSource
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.GithubUserPagingSource
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.RemoteDataSource
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.GithubUser
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.SortType
import com.agussetiawan.application.githubcleanarchitecture.core.domain.repository.IGithubUserRepository
import com.agussetiawan.application.githubcleanarchitecture.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GithubUserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IGithubUserRepository {
    override fun searchUsers(
        query: String,
        sort: String?,
        order: String?
    ): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20
            ),
            remoteMediator = GithubUserRemoteMediator(
                localDataSource,
                remoteDataSource,
                query,
                sort,
                order,
            ),
            pagingSourceFactory = {
                localDataSource.searchUsers(query)
            }
        ).flow.map {
            it.map { entity ->
                DataMapper.mapEntityToDomain(entity)
            }
        }
    }

    override fun getFavoriteUsers(): Flow<List<GithubUser>> =
        localDataSource.getFavoriteUsers().map {
            DataMapper.mapEntitiesToDomain(it)
        }

    override suspend fun setFavoriteUser(user: GithubUser) =
        localDataSource.setFavoriteUser(user.id.toString(), user.isFavorite)


    override suspend fun deleteFavorite(userId: Int) =
        localDataSource.deleteFavorite(userId.toString())

    override fun getSortType(): Flow<SortType> {
        return localDataSource.getSortType()
    }

    override suspend fun saveSortType(sortType: SortType) {
        return localDataSource.saveSortType(sortType)
    }
}