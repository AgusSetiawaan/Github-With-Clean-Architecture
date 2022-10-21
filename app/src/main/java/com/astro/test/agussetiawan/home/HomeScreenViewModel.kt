package com.astro.test.agussetiawan.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.astro.test.agussetiawan.core.domain.model.DataItem
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.core.domain.model.SortType
import com.astro.test.agussetiawan.core.domain.usecase.GithubUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val useCase: GithubUserUseCase): ViewModel() {

    val queryChannel = MutableStateFlow("")
    private var query: String = ""

    var searchResultLiveData: LiveData<PagingData<GithubUser>> = MutableLiveData()
        private set

    val searchResult = queryChannel
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .combine(useCase.getSortType()){
            string: String, sortType: SortType ->
            Pair(string, sortType)
        }
        .flatMapLatest{
            query = it.first
            useCase.searchUsers(it.first, "followers",it.second)
        }
        .asLiveData().cachedIn(viewModelScope)

    fun sortList(sortType: SortType): LiveData<PagingData<DataItem>> {
        viewModelScope.launch {
            useCase.setSortType(sortType)
        }
        return useCase.searchUsers(query, "followers", sortType)
            .asLiveData().cachedIn(viewModelScope)
    }

    fun getSortType() = useCase.getSortType().asLiveData()

    fun setFavoriteUser(user: GithubUser) {
        viewModelScope.launch {
            useCase.setFavoriteUser(user)
        }
    }

    fun deleteFavoriteUser(id: Int){
        viewModelScope.launch {
            useCase.deleteFavorite(id)
        }
    }

}