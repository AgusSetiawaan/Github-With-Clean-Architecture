package com.astro.test.core.di

import android.content.Context
import androidx.room.Room
import com.astro.test.core.data.source.local.room.GithubUserDao
import com.astro.test.core.data.source.local.room.GithubUserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideGithubUserDatabase(@ApplicationContext context: Context): GithubUserDatabase =
        Room.databaseBuilder(
            context,
            GithubUserDatabase::class.java, "GithubUser.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideGithubUserDao(database: GithubUserDatabase): GithubUserDao =
        database.githubUserDao()
}