@file:OptIn(ExperimentalCoroutinesApi::class)

package com.astro.test.agussetiawan.home

import android.os.Bundle
import android.util.Log
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
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.astro.test.agussetiawan.adapter.HomeScreenAdapter
import com.astro.test.agussetiawan.adapter.LoadingStateAdapter
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.databinding.ListUserLayoutBinding
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

        adapter = HomeScreenAdapter(::onFavoriteClick)

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
    }

    private fun setListData(listData: PagingData<GithubUser>){
        adapter.submitData(lifecycle, listData)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvGithubUsers.layoutManager = layoutManager
        binding.rvGithubUsers.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )

        lifecycleScope.launch{
            adapter.loadStateFlow.collect{ loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                // show empty list
                binding.tvNoResult.isVisible = isListEmpty
                // Only show the list if refresh succeeds.
                binding.rvGithubUsers.isVisible = !isListEmpty

                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }
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


}