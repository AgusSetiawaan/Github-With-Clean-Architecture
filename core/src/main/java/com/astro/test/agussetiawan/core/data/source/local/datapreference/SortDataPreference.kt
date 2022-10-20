package com.astro.test.agussetiawan.core.data.source.local.datapreference

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.astro.test.agussetiawan.core.domain.model.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SortDataPreference @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun getSortType(): Flow<SortType>{
        Log.d(TAG, "masuk sini")
        return dataStore.data.map {pref ->
            SortType.values().firstOrNull { it.desc ==  pref[SORT_TYPE_KEY]}?:SortType.ASC
        }.catch {exception ->
            Log.d(TAG, "getSortType: $exception")
        }
    }

    suspend fun saveSortType(sortType: SortType){
        dataStore.edit {
            it[SORT_TYPE_KEY] = sortType.desc
        }
    }

    companion object{
        private const val TAG = "SortDataPreference"

        private val SORT_TYPE_KEY = stringPreferencesKey("sort_type")
    }
}