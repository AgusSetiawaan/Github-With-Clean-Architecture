package com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.network

import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.response.GithubSearchUserResponse
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.response.GithubUserDetailResponse
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.response.GithubUserReposResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") count: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null
    ): Response<GithubSearchUserResponse>

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): Response<GithubUserDetailResponse>

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String
    ): Response<List<GithubUserReposResponse>>

}