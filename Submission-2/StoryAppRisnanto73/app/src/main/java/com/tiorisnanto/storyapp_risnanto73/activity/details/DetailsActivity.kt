package com.tiorisnanto.storyapp_risnanto73.activity.details

import android.graphics.text.LineBreaker
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STORY = "story"
    }

    private val details = DetailsViewModel()
    private lateinit var stories: ListStoriesItem
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            run {
                binding.tvDescription.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
        }

        stories = intent.getParcelableExtra(EXTRA_STORY)!!
        details.setDetailStories(stories)
        displayResult()

        supportActionBar?.title = "Detail Stories"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun displayResult() {
        with(binding){
            tvName.text = details.storiesItem.name
            tvDescription.text = details.storiesItem.description

            Glide.with(ivStory)
                .load(details.storiesItem.photoUrl) // URL Avatar
                .placeholder(R.drawable.ic_baseline_library_books_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(ivStory)
        }
    }
}