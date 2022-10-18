package com.astro.test.core.domain.model

data class GithubUser(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val isFavorite: Boolean
)