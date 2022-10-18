package com.astro.test.core.data.source.remote

import com.astro.test.core.data.source.remote.network.ApiResponse
import com.astro.test.core.data.source.remote.network.ApiService
import com.astro.test.core.data.source.remote.response.GithubUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    fun getSearchUsers(
        query: String,
        count: Int,
        page: Int,
        sort: String,
        order: String): Flow<ApiResponse<List<GithubUserResponse>>> = flow {
            try {
                val response = apiService.searchUsers(query, count, page, sort, order)
                val dataArray = response.items
                if(dataArray.isEmpty()){
                    emit(ApiResponse.Empty)
                }
                else{
                    emit(ApiResponse.Success(dataArray))
                }
            }
            catch (e: Exception){
                emit(ApiResponse.Error(e))
            }
    }.flowOn(Dispatchers.IO)
}