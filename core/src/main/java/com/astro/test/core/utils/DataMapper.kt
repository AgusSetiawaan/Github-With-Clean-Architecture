package com.astro.test.core.utils

import com.astro.test.core.data.source.local.entity.GithubUserEntity
import com.astro.test.core.data.source.remote.response.GithubUserResponse
import com.astro.test.core.domain.model.GithubUser

object DataMapper {

    fun mapEntitiesToDomain(list: List<GithubUserEntity>): List<GithubUser> =
        list.map {
            GithubUser(
                id = it.id,
                username = it.username,
                avatarUrl = it.avatarUrl,
                isFavorite = it.isFavorite
            )
        }

    fun mapDomainToEntity(user: GithubUser): GithubUserEntity =
        GithubUserEntity(
            id = user.id,
            username = user.username,
            avatarUrl = user.avatarUrl,
            isFavorite = true
        )

    fun mapResponseToDomain(response: GithubUserResponse): GithubUser =
        GithubUser(
            id = response.id,
            username = response.login,
            avatarUrl = response.avatarUrl,
            isFavorite = false
        )
}