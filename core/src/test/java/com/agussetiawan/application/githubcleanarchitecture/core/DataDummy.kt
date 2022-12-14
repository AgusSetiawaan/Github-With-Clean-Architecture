package com.agussetiawan.application.githubcleanarchitecture.core

import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.GithubUser

object DataDummy {

    fun generateDummyGithubUser(): List<GithubUser>{
        val items: MutableList<GithubUser> = arrayListOf()
        for(i in 0..100){
            items.add(
                GithubUser(
                    id = 0,
                    username = "username $i",
                    avatarUrl = "avatar $i",
                    isFavorite = true
                )
            )
        }
        return items
    }
}