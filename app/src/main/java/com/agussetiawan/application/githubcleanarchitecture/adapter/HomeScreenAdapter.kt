package com.agussetiawan.application.githubcleanarchitecture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.agussetiawan.application.githubcleanarchitecture.R
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.DataItem
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.GithubUser
import com.agussetiawan.application.githubcleanarchitecture.core.domain.model.SortType
import com.agussetiawan.application.githubcleanarchitecture.databinding.ItemRadioButtonSortBinding
import com.agussetiawan.application.githubcleanarchitecture.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class HomeScreenAdapter(private val onFavoriteClick: (GithubUser) -> Unit,
                        private val onRadioButtonClick: (SortType) -> Unit): PagingDataAdapter<DataItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            HEADER_ITEM_VIEW_TYPE -> {
                val binding = ItemRadioButtonSortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SortViewHolder(binding, onRadioButtonClick)
            }
            else -> {
                val binding =
                    ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GithubUserItemViewHolder(binding, parent.context, onFavoriteClick)
            }
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            if(it is DataItem.Header){
                (holder as SortViewHolder).bind(it.sortType)
            }
            else{
                (holder as GithubUserItemViewHolder).bind((it as DataItem.UserItem).user)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> HEADER_ITEM_VIEW_TYPE
            else -> DATA_ITEM_VIEW_TYPE
        }
    }

    fun changeFavState(user: GithubUser) {
        val data = snapshot().items.find { it.id == user.id }
        val position = snapshot().items.indexOf(data)
        data?.let {
            if(data is DataItem.UserItem){
                data.user.isFavorite = user.isFavorite
            }
        }
        notifyItemChanged(position)
    }


    inner class GithubUserItemViewHolder(
        private val binding: ItemUserBinding,
        private val context: Context,
        private val onFavoriteClick: (GithubUser) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GithubUser) {
            binding.name.text = user.username
            Glide.with(context)
                .load(user.avatarUrl)
                .into(binding.userAvatar)

            binding.btnFavorite.setImageDrawable(
                if (user.isFavorite) ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_love_active
                )
                else ContextCompat.getDrawable(context, R.drawable.ic_love_inactive)
            )

            binding.btnFavorite.setOnClickListener {
                onFavoriteClick(user)
            }
        }
    }

    inner class SortViewHolder(private val binding: ItemRadioButtonSortBinding,
                               private val onRadioButtonClick: (SortType) -> Unit): RecyclerView.ViewHolder(binding.root){
        fun bind(sortType: SortType){
            if(sortType == SortType.ASC){
                binding.radioAsc.isChecked = true
            }
            else{
                binding.radioDesc.isChecked = true
            }

            // onClick
            binding.radioAsc.setOnClickListener{
                if(it is RadioButton){
                    if(it.isChecked){
                        onRadioButtonClick(SortType.ASC)
                    }
                }
            }
            binding.radioDesc.setOnClickListener{
                if(it is RadioButton){
                    if(it.isChecked){
                        onRadioButtonClick(SortType.DESC)
                    }
                }
            }
        }
    }

    companion object {

        private const val HEADER_ITEM_VIEW_TYPE = 0
        private const val DATA_ITEM_VIEW_TYPE = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}