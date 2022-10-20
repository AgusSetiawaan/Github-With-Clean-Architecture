package com.astro.test.agussetiawan.core.domain.model

sealed class DataItem{
    abstract val id: Int

    data class UserItem(val user: GithubUser): DataItem(){
        override val id = user.id
    }

    data class Header(val sortType: SortType): DataItem(){
        override val id: Int
            get() = Int.MIN_VALUE
    }
}