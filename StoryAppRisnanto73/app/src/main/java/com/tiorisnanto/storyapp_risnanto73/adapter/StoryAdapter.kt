package com.tiorisnanto.storyapp_risnanto73.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.details.DetailsActivity
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoryItem
import com.tiorisnanto.storyapp_risnanto73.databinding.ItemListStoryBinding
import com.tiorisnanto.storyapp_risnanto73.helper.DiffCallback
import java.util.*

class StoryAdapter :  RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private val listStory = ArrayList<ListStoryItem>()

    fun setListStory(itemStory: List<ListStoryItem>) {
        val diffCallback = DiffCallback(this.listStory, itemStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.listStory.clear()
        this.listStory.addAll(itemStory)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount() = listStory.size

    inner class ViewHolder(private var binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            with(binding) {
                Glide.with(imgItemImage)
                    .load(story.photoUrl) // URL Avatar
                    .placeholder(R.drawable.ic_baseline_library_books_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(imgItemImage)
                tvName.text = story.name
                tvDescription.text = story.description

                imgItemImage.setOnClickListener {
                    val intent = Intent(it.context, DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.EXTRA_STORY, story)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}