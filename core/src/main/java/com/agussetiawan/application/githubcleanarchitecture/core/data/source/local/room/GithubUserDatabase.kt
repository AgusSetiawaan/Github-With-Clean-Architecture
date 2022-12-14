package com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.GithubUserEntity
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.GithubUserFavoriteEntity
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.RemoteKeys
import com.example.submissionstoryapp.data.local.RemoteKeysDao

@Database(entities = [GithubUserEntity::class, RemoteKeys::class, GithubUserFavoriteEntity::class], version = 1, exportSchema = false)
abstract class GithubUserDatabase: RoomDatabase() {

    abstract fun githubUserDao(): GithubUserDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}