package com.astro.test.core.data.source.local.room

import androidx.room.*
import com.astro.test.core.data.source.local.entity.GithubUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setFavoriteUser(githubUser: GithubUserEntity)

    @Query("DELETE FROM github_user WHERE id=:id")
    suspend fun deleteFavorite(id: Int)

    @Query("SELECT * FROM github_user WHERE isFavorite = 1")
    fun getFavoriteUsers(): Flow<List<GithubUserEntity>>
}