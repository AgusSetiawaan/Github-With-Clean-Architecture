package com.astro.test.agussetiawan.core.data.source.remote

import com.astro.test.agussetiawan.core.data.source.remote.network.ApiResponse
import com.astro.test.agussetiawan.core.data.source.remote.network.ApiService
import com.astro.test.agussetiawan.core.data.source.remote.response.GithubSearchUserResponse
import com.astro.test.agussetiawan.core.data.source.remote.response.GithubUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getSearchUsers(
        query: String,
        count: Int,
        page: Int,
        sort: String?,
        order: String?): Response<GithubSearchUserResponse> = apiService.searchUsers(query, count, page, sort, order)
}