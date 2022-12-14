package com.agussetiawan.application.githubcleanarchitecture.core.utils

import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.GithubUserEntity
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.UserWithFavorite
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.remote.response.GithubUserResponse
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.GithubUser

object DataMapper {

    fun mapEntitiesToDomain(list: List<GithubUserEntity>): List<GithubUser> =
        list.map {
            GithubUser(
                id = it.id.toInt(),
                username = it.username,
                avatarUrl = it.avatarUrl,
                isFavorite = it.isFavorite
            )
        }

    fun mapDomainToEntity(user: GithubUser): GithubUserEntity =
        GithubUserEntity(
            id = user.id.toString(),
            username = user.username,
            avatarUrl = user.avatarUrl,
            query = "",
            isFavorite = true
        )

    fun mapEntityToDomain(entity: UserWithFavorite): GithubUser =
        GithubUser(
            id = entity.user.id.toInt(),
            username = entity.user.username,
            avatarUrl = entity.user.avatarUrl,
            isFavorite = entity.favorite?.isFavorite?:false
        )

    fun mapResponseToDomain(response: GithubUserResponse, list: List<GithubUserEntity>): GithubUser {
        val user = list.find { it.id == response.id.toString() }
        return GithubUser(
            id = response.id,
            username = response.login,
            avatarUrl = response.avatarUrl,
            isFavorite = user?.isFavorite?:false
        )
    }

    fun mapResponseToEntity(list: List<GithubUserResponse>, query: String): List<GithubUserEntity> {
        return list.map {
            GithubUserEntity(
                id = it.id.toString(),
                username = it.login,
                avatarUrl = it.avatarUrl,
                query = query,
                isFavorite = false
            )
        }
    }

}