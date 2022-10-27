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
import com.tiorisnanto.storyapp_risnanto73.databinding.ItemListStoryBinding
import androidx.core.util.Pair
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem

class StoriesAdapter :
    PagingDataAdapter<ListStoriesItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ViewHolder(private var binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoriesItem) {
            with(binding) {
                Glide.with(imgItemImage)
                    .load(story.photoUrl) // URL Avatar
                    .placeholder(R.drawable.ic_baseline_library_books_24)
                    .error(R.drawable.ic_baseline_library_books_24)
                    .into(imgItemImage)
                tvName.text = story.name
                tvDescription.text = story.description

                // image OnClickListener
                imgItemImage.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imgItemImage, "image"),
                            Pair(tvName, "name"),
                            Pair(tvDescription, "description"),
                        )

                    val intent = Intent(it.context, DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.EXTRA_STORY, story)
                    it.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoriesItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoriesItem,
                newItem: ListStoriesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoriesItem,
                newItem: ListStoriesItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}