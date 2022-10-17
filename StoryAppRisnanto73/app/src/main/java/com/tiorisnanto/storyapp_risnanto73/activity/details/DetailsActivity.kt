package com.tiorisnanto.storyapp_risnanto73.activity.details

import android.graphics.text.LineBreaker
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoryItem
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STORY = "story"
    }

    private val details = DetailsViewModel()
    private lateinit var story: ListStoryItem
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

        story = intent.getParcelableExtra(EXTRA_STORY)!!
        details.setDetailStory(story)
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
            tvName.text = details.storyItem.name
            tvDescription.text = details.storyItem.description

            Glide.with(ivStory)
                .load(details.storyItem.photoUrl) // URL Avatar
                .placeholder(R.drawable.ic_baseline_library_books_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(ivStory)
        }
    }
}