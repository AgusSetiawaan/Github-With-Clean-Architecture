package com.astro.test.core.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.astro.test.core.data.source.remote.network.ApiResponse
import com.astro.test.core.data.source.remote.network.ApiService
import com.astro.test.core.data.source.remote.response.GithubUserResponse

class GithubUserPagingSource(
    private val apiService: ApiService,
    private val query: String,
    private val sort: String,
    private val order: String
): PagingSource<Int, GithubUserResponse>() {

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUserResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUserResponse> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.searchUsers(query, params.loadSize, position, sort, order)

            val dataList = responseData.items

            LoadResult.Page(
                data = dataList,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (dataList.isEmpty()) null else position + 1
            )
        }

        catch (e: Exception){
            return LoadResult.Error(e)
        }


    }
}