package com.astro.test.agussetiawan.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.agussetiawan.R
import com.astro.test.agussetiawan.core.domain.model.GithubUser
import com.astro.test.agussetiawan.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class HomeScreenAdapter(private val onFavoriteClick: (GithubUser) -> Unit): PagingDataAdapter<GithubUser, HomeScreenAdapter.GithubUserItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserItemViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubUserItemViewHolder(binding, parent.context, onFavoriteClick)

    }

    override fun onBindViewHolder(holder: GithubUserItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun changeFavState(user: GithubUser) {
        val data = snapshot().items.find { it.id == user.id }
        val position = snapshot().items.indexOf(data)
        if (data != null) {
            data.isFavorite = user.isFavorite
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

    companion object {

        private const val ITEM_VIEW_HEADER = 0
        private const val ITEM_VIEW_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GithubUser>() {
            override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}