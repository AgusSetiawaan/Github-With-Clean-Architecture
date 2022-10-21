package com.astro.test.agussetiawan.core.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.astro.test.agussetiawan.core.data.source.local.LocalDataSource
import com.astro.test.agussetiawan.core.data.source.remote.GithubUserPagingSource
import com.astro.test.agussetiawan.core.data.source.remote.RemoteDataSource
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.core.domain.model.SortType
import com.astro.test.agussetiawan.core.domain.repository.IGithubUserRepository
import com.astro.test.agussetiawan.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
            pagingSourceFactory = {
                GithubUserPagingSource(
                    remoteDataSource,
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

    override fun getSortType(): Flow<SortType> {
        Log.d("repository", "masuk sini")
        return localDataSource.getSortType()
    }

    override suspend fun saveSortType(sortType: SortType) {
        return localDataSource.saveSortType(sortType)
    }
}