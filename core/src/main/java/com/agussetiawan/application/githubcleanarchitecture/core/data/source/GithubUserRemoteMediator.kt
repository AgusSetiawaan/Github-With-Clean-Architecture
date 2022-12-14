package com.agussetiawan.application.githubcleanarchitecture.core.data.source

import android.provider.MediaStore
import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.LocalDataSource
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.GithubUserEntity
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.RemoteKeys
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.UserWithFavorite
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.room.GithubUserDatabase
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.RemoteDataSource
import com.agussetiawan.application.githubcleanarchitecture.core.utils.DataMapper
import com.agussetiawan.application.githubcleanarchitecture.core.utils.parseErrorBody
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GithubUserRemoteMediator(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val query: String,
    private val sort: String?,
    private val order: String?,
): RemoteMediator<Int, UserWithFavorite>() {

    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserWithFavorite>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = remoteDataSource.getSearchUsers(query, state.config.pageSize, page, sort, order)

            if(responseData.isSuccessful){
                val listGithubUser = responseData.body()?.items?: listOf()

                val endOfPaginationReached = listGithubUser.isEmpty()

                localDataSource.getDatabase().withTransaction {
                    if(loadType == LoadType.REFRESH) {
                        localDataSource.deleteRemoteKeys()
                        localDataSource.deleteAllUsers()
                    }

                    val prevKey = if(page == 1) null else page -1
                    val nextKey = if(endOfPaginationReached) null else page + 1
                    val keys = listGithubUser.map {
                        RemoteKeys(it.id.toString(), prevKey, nextKey)
                    }
                    localDataSource.insertAllRemoteKeys(keys)

                    val githubUserEntities = DataMapper.mapResponseToEntity(listGithubUser, query)
                    localDataSource.insertAllUsers(githubUserEntities)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
            else{
                val errorResponse = parseErrorBody(responseData.errorBody())
                val throwable = Throwable(message = errorResponse.message)
                return MediatorResult.Error(throwable)
            }

        }
        catch (e: Exception){
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UserWithFavorite>): RemoteKeys?{
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { data ->
            localDataSource.getRemoteKeys(data.user.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserWithFavorite>): RemoteKeys?{
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { data ->
            localDataSource.getRemoteKeys(data.user.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UserWithFavorite>): RemoteKeys?{
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.user?.id?.let { id ->
                localDataSource.getRemoteKeys(id)
            }
        }
    }

}