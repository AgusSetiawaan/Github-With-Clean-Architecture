package com.astro.test.agussetiawan.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.astro.test.agussetiawan.core.data.source.local.LocalDataSource
import com.astro.test.agussetiawan.core.data.source.remote.GithubUserPagingSource
import com.astro.test.agussetiawan.core.data.source.remote.network.ApiService
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.core.domain.repository.IGithubUserRepository
import com.astro.test.agussetiawan.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GithubUserRepository @Inject constructor(
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource
)
    : IGithubUserRepository {
    override fun searchUsers(
        query: String,
        sort: String?,
        order: String?
    ): Flow<PagingData<GithubUser>> {
        //TODO: check favorite users in database before get list
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                GithubUserPagingSource(
                    apiService,
                    query,
                    sort,
                    order,
                    localDataSource.getFavoriteUsers()
                )
            }
        ).flow
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