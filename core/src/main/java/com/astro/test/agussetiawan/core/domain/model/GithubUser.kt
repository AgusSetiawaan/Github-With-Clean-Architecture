package com.astro.test.agussetiawan.core.domain.model

data class GithubUser(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    var isFavorite: Boolean
)