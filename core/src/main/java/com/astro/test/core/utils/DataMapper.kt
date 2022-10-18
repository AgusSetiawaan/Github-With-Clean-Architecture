package com.astro.test.core.utils

import com.astro.test.core.data.source.local.entity.GithubUserEntity
import com.astro.test.core.domain.model.GithubUser

object DataMapper {

    fun mapEntitiesToDomain(list: List<GithubUserEntity>): List<GithubUser> =
        list.map {
            GithubUser(
                id = it.id,
                username = it.username,
                name = it.name,
                isFavorite = it.isFavorite
            )
        }

    fun mapDomainToEntity(user: GithubUser): GithubUserEntity =
        GithubUserEntity(
            id = user.id,
            username = user.username,
            name = user.name,
            isFavorite = true
        )
}