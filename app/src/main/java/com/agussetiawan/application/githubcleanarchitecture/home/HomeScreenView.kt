@file:OptIn(ExperimentalCoroutinesApi::class)

package com.agussetiawan.application.githubcleanarchitecture.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.agussetiawan.application.githubcleanarchitecture.adapter.HomeScreenAdapter
import com.agussetiawan.application.githubcleanarchitecture.adapter.LoadingStateAdapter
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.DataItem
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.GithubUser
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.SortType
import com.agussetiawan.application.githubcleanarchitecture.databinding.ListUserLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@AndroidEntryPoint
class HomeScreenView: Fragment() {

    private lateinit var binding: ListUserLayoutBinding
    private val viewModel: HomeScreenViewModel by viewModels()

    private lateinit var adapter: HomeScreenAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListUserLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeScreenAdapter(::onFavoriteClick, ::onSortSelect)

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                lifecycleScope.launch {
                    viewModel.queryChannel.value = newText.toString()
                }
                return true
            }

        })

        viewModel.searchResult.observe(viewLifecycleOwner){
            setListData(it)
        }

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvGithubUsers.layoutManager = layoutManager
        binding.rvGithubUsers.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
    }

    private fun setListData(listData: PagingData<DataItem>){
        adapter.submitData(lifecycle, listData)

//        lifecycleScope.launch{
//            adapter.loadStateFlow.collect{ loadState ->
//                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount - 1 == 0
//                // show empty list
//                binding.tvNoResult.isVisible = isListEmpty
//                // Only show the list if refresh succeeds.
//                binding.rvGithubUsers.isVisible = !isListEmpty
//                // Show loading spinner during initial load or refresh.
//                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
//                // Show the retry state if initial load or refresh fails.
//                binding.errorLayout.isVisible = loadState.source.refresh is LoadState.Error
//
//                val errorState = loadState.source.append as? LoadState.Error
//                    ?: loadState.source.prepend as? LoadState.Error
//                    ?: loadState.append as? LoadState.Error
//                    ?: loadState.prepend as? LoadState.Error
//                errorState?.let {
//                    binding.errorMsg.text = it.error.localizedMessage
//                }
//
//                if(loadState.source.refresh is LoadState.Error){
//                    binding.errorMsg.text = (loadState.refresh as LoadState.Error).error.localizedMessage
//                }
//            }
//        }

        //onClick Listener retry button
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun onFavoriteClick(user: GithubUser){
        if(user.isFavorite){
            user.isFavorite = false
            viewModel.deleteFavoriteUser(user.id)
        }
        else{
            user.isFavorite = true
            viewModel.setFavoriteUser(user)
        }
        adapter.changeFavState(user)
    }

    private fun onSortSelect(sortType: SortType){
        adapter.submitData(lifecycle, PagingData.empty())
        viewModel.sortList(sortType).observe(viewLifecycleOwner){
            setListData(it)
        }
    }

}