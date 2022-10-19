package com.astro.test.agussetiawan.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_user")
data class GithubUserEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val avatarUrl: String,
    var isFavorite: Boolean
)
