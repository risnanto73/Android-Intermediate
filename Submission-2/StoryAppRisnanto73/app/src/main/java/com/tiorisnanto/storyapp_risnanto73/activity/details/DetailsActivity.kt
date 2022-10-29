package com.tiorisnanto.storyapp_risnanto73.activity.details

import android.graphics.text.LineBreaker
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STORIES = "stories"
    }

    private val details = DetailsViewModel()
    private lateinit var stories: ListStoriesItem
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Support Action Bar
        supportActionBar?.title = getString(R.string.detail_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            run {
                binding.tvDescription.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
        }

        stories = intent.getParcelableExtra(EXTRA_STORIES)!!
        details.setDetailStories(stories)
        displayResults()
        imageBack()


    }

    private fun imageBack() {
        binding.ivStories.setOnClickListener {
            Toast.makeText(this, getString(R.string.back_success), Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun displayResults() {
        binding.tvName.text = details.storiesItem.name
        binding.tvDescription.text = details.storiesItem.description
        Glide.with(binding.ivStories)
            .load(details.storiesItem.photoUrl)
            .placeholder(R.drawable.ic_baseline_library_books_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.ivStories)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}