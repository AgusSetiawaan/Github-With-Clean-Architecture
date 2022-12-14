package com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote

import androidx.paging.RemoteMediator
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.network.ApiResponse
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.network.ApiService
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.response.GithubSearchUserResponse
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.response.GithubUserDetailResponse
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.response.GithubUserReposResponse
import com.agussetiawan.application.githubcleanarchitecture.core.utils.parseErrorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getSearchUsers(
        query: String,
        count: Int,
        page: Int,
        sort: String?,
        order: String?): Response<GithubSearchUserResponse> = apiService.searchUsers(query, count, page, sort, order)

    fun getUserDetail(username: String): Flow<ApiResponse<GithubUserDetailResponse>> = flow {
        try {
            apiService.getUserDetail(username).apply {
                if(this.isSuccessful){
                    this.body()?.let {
                        emit(ApiResponse.Success(it))
                    }
                }
                else{
                    val errorResponse = parseErrorBody(this.errorBody())
                    emit(ApiResponse.Error(errorResponse.message))
                }
            }
        }
        catch (e: Exception){
            emit(ApiResponse.Error(e.toString()))
        }
    }

    fun getUserRepos(username: String): Flow<ApiResponse<List<GithubUserReposResponse>>> = flow {
        try {
            apiService.getUserRepos(username).apply {
                if(this.isSuccessful){
                    this.body()?.let {
                        if(it.isEmpty()){
                            emit(ApiResponse.Empty)
                        }
                        else{
                            emit(ApiResponse.Success(it))
                        }
                    }
                }
                else{
                    val errorResponse = parseErrorBody(this.errorBody())
                    emit(ApiResponse.Error(errorResponse.message))
                }
            }
        }
        catch (e: Exception){
            emit(ApiResponse.Error(e.toString()))
        }
    }
}