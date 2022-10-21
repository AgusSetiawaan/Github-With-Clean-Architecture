package com.astro.test.agussetiawan.core

import com.astro.test.agussetiawan.core.domain.model.GithubUser

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