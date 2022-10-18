package com.astro.test.core.data.source.remote.network

import com.astro.test.core.data.source.remote.response.GithubSearchUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") count: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): GithubSearchUserResponse

}