package com.astro.test.agussetiawan.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.astro.test.agussetiawan.core.data.source.local.entity.GithubUserEntity

@Database(entities = [GithubUserEntity::class], version = 1, exportSchema = false)
abstract class GithubUserDatabase: RoomDatabase() {

    abstract fun githubUserDao(): GithubUserDao
}