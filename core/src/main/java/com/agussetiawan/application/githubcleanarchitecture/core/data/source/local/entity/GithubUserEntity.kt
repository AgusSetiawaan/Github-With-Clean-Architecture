package com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "github_user")
data class GithubUserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val avatarUrl: String,
    val query: String,
    var isFavorite: Boolean
)

@Entity(tableName = "favorite_user",)
data class GithubUserFavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId: String,
    val isFavorite: Boolean
)

data class UserWithFavorite(
    @Embedded
    val user: GithubUserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val favorite: GithubUserFavoriteEntity?
)

