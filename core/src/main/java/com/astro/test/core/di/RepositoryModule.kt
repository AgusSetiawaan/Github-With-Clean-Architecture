package com.astro.test.core.di

import com.astro.test.core.data.GithubUserRepository
import com.astro.test.core.domain.repository.IGithubUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repository: GithubUserRepository): IGithubUserRepository
}