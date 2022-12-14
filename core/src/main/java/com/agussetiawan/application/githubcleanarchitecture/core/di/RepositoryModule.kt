package com.agussetiawan.application.githubcleanarchitecture.core.di

import com.agussetiawan.application.githubcleanarchitecture.core.data.GithubUserRepository
import com.agussetiawan.application.githubcleanarchitecture.core.domain.repository.IGithubUserRepository
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