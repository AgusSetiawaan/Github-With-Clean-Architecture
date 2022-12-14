package com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.*
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.GithubUserEntity
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.GithubUserFavoriteEntity
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.UserWithFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGithubUsers(githubUsers: List<GithubUserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteUser(favoriteUser: GithubUserFavoriteEntity)

    @Query("SELECT COUNT(*) FROM favorite_user WHERE userId=:userId")
    fun getRowCount(userId: String): Int

    @Query("UPDATE favorite_user SET isFavorite =:favoriteState WHERE userId=:userId")
    suspend fun updateFavoriteUser(userId: String, favoriteState: Boolean)

    @Query("SELECT * FROM github_user where `query`=:queryString")
    fun getAllGithubUsers(queryString: String): PagingSource<Int, UserWithFavorite>

    @Query("DELETE FROM favorite_user WHERE userId=:userId")
    suspend fun deleteFavorite(userId: String)

    @Query("SELECT * FROM github_user WHERE isFavorite = 1")
    fun getFavoriteUsers(): Flow<List<GithubUserEntity>>

    @Query("DELETE FROM github_user")
    suspend fun deleteAllUsers()
}