package com.astro.test.agussetiawan.core.data.source.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.astro.test.agussetiawan.core.data.source.local.entity.GithubUserEntity
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.core.utils.DataMapper
import com.astro.test.agussetiawan.core.utils.parseErrorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GithubUserPagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val query: String,
    private val sort: String?,
    private val order: String?,
    private val favoriteUsers: Flow<List<GithubUserEntity>>
): PagingSource<Int, GithubUser>() {

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
        try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = remoteDataSource.getSearchUsers(query, params.loadSize, position, sort, order)

            if(responseData.isSuccessful){
                val responseBody = responseData.body()
                val dataList = responseBody?.items?.map {
                    DataMapper.mapResponseToDomain(it, favoriteUsers.first())
                }

                return LoadResult.Page(
                    data = dataList?: listOf(),
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (dataList.isNullOrEmpty()) null else position + 1
                )
            }
            else{
                val errorResponse = parseErrorBody(responseData.errorBody())
                val throwable = Throwable(message = errorResponse.message)
                Log.d("pagingsource", throwable.message?:"kosong")
                return LoadResult.Error(throwable)
            }


        }

        catch (e: Exception){
            return LoadResult.Error(e)
        }


    }
}