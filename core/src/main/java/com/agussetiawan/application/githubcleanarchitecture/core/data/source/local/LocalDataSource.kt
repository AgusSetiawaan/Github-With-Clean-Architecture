package com.agussetiawan.application.githubcleanarchitecture.core.data.source.local

import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.datapreference.SortDataPreference
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.GithubUserEntity
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.GithubUserFavoriteEntity
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.entity.RemoteKeys
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.room.GithubUserDao
import com.agussetiawan.application.githubcleanarchitecture.core.data.source.local.room.GithubUserDatabase
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.SortType
import com.example.submissionstoryapp.data.local.RemoteKeysDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: GithubUserDao,
                                          private val database: GithubUserDatabase,
                                          private val remoteKeysDao: RemoteKeysDao,
                                          private val sortDataPreference: SortDataPreference) {

    suspend fun setFavoriteUser(userId: String, isFavorite: Boolean){
        dao.insertFavoriteUser(GithubUserFavoriteEntity(
            userId = userId,
            isFavorite = isFavorite
        ))
    }

    suspend fun deleteFavorite(userId: String) = dao.deleteFavorite(userId)

    fun getFavoriteUsers(): Flow<List<GithubUserEntity>> = dao.getFavoriteUsers()

    suspend fun insertAllUsers(list: List<GithubUserEntity>) = dao.insertGithubUsers(list)

    fun searchUsers(query: String) = dao.getAllGithubUsers(query)

    suspend fun deleteAllUsers() = dao.deleteAllUsers()

    fun getSortType(): Flow<SortType> {
        return sortDataPreference.getSortType()
    }

    suspend fun saveSortType(sortType: SortType) = sortDataPreference.saveSortType(sortType)

    suspend fun insertAllRemoteKeys(remoteKeys: List<RemoteKeys>) = remoteKeysDao.insertAll(remoteKeys)

    suspend fun getRemoteKeys(id: String): RemoteKeys? = remoteKeysDao.getRemoteKeysId(id)

    suspend fun deleteRemoteKeys() = remoteKeysDao.deleteRemoteKeys()

    fun getDatabase(): GithubUserDatabase = database
}