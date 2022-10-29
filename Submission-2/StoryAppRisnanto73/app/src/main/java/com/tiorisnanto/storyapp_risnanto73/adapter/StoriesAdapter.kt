package com.tiorisnanto.storyapp_risnanto73.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.details.DetailsActivity
import com.tiorisnanto.storyapp_risnanto73.databinding.ItemListStoriesBinding
import androidx.core.util.Pair
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem

class StoriesAdapter :
    PagingDataAdapter<ListStoriesItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ViewHolder(private var binding: ItemListStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stories: ListStoriesItem) {
            Glide.with(binding.imgItemImage)
                // URL Avatar
                .load(stories.photoUrl)
                .placeholder(R.drawable.ic_baseline_library_books_24)
                .error(R.drawable.ic_baseline_library_books_24)
                .into(binding.imgItemImage)
            binding.tvName.text = stories.name
            binding.tvDescription.text = stories.description

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.EXTRA_STORIES, stories)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    binding.root.context as Activity,
                    Pair.create(binding.imgItemImage, "image"),
                    Pair.create(binding.tvName, "name"),
                    Pair.create(binding.tvDescription, "description")
                )
                binding.root.context.startActivity(intent, options.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoriesItem>() {
            override fun areItemsTheSame(
                oldItems: ListStoriesItem,
                newItems: ListStoriesItem
            ): Boolean {
                return oldItems == newItems
            }

            override fun areContentsTheSame(
                oldItems: ListStoriesItem,
                newItems: ListStoriesItem
            ): Boolean {
                return oldItems.id == newItems.id
            }
        }
    }
}