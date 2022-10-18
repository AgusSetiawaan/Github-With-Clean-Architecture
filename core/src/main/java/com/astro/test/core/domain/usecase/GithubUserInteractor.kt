package com.astro.test.core.domain.usecase

import androidx.paging.PagingData
import com.astro.test.core.domain.model.GithubUser
import com.astro.test.core.domain.repository.IGithubUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubUserInteractor @Inject constructor(private val repository: IGithubUserRepository): GithubUserUseCase {
    override fun searchUsers(
        query: String,
        sort: String,
        order: String
    ): Flow<PagingData<GithubUser>> {
        return repository.searchUsers(query, sort, order)
    }

    override fun getFavoriteUsers(): Flow<List<GithubUser>> {
        return repository.getFavoriteUsers()
    }

    override suspend fun setFavoriteUser(user: GithubUser) {
        return repository.setFavoriteUser(user)
    }

    override suspend fun deleteFavorite(userId: Int) {
        return repository.deleteFavorite(userId)
    }
}