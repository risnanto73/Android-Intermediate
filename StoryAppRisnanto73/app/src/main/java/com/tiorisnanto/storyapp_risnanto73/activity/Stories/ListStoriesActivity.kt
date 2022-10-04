package com.tiorisnanto.storyapp_risnanto73.activity.Stories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.AddStroies.AddStroiesActivity
import com.tiorisnanto.storyapp_risnanto73.adapter.StoryAdapter
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityListStoriesBinding

class ListStoriesActivity : AppCompatActivity() {

    private var _binding: ActivityListStoriesBinding? = null
    private val binding get() = _binding

    private lateinit var user: UserModel
    private lateinit var adapter: StoryAdapter
    private val viewModel = ListStoriesViewModel()

    companion object {
        const val EXTRA_USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListStoriesBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()
        addStoryAction()
        user = intent.getParcelableExtra(EXTRA_USER)!!

        setListStory()
        adapter = StoryAdapter()

        showSnackBar()

        setupRecycleView()

        showLoading()
        showHaveDataOrNot()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showHaveDataOrNot() {
        viewModel.isHaveData.observe(this){
            binding?.apply {
                if (it) {
                    rvStory.visibility = View.VISIBLE
                    tvInfo.visibility = View.GONE
                } else {
                    rvStory.visibility = View.GONE
                    tvInfo.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) {
            binding?.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    rvStory.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    rvStory.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupRecycleView() {
        binding?.rvStory?.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding?.rvStory?.setHasFixedSize(true)
        binding?.rvStory?.adapter = adapter
    }

    private fun showSnackBar() {
        viewModel.snackBarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    findViewById(R.id.rv_story),
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setListStory() {
        viewModel.showListStory(user.token)
        viewModel.itemStory.observe(this) {
            adapter.setListStory(it)
        }
    }

    override fun onResume() {
        super.onResume()
        setListStory()
    }

    private fun addStoryAction() {
        binding?.ivAddStory?.setOnClickListener {
            val moveToAddStoryActivity = Intent(this, AddStroiesActivity::class.java)
            moveToAddStoryActivity.putExtra(AddStroiesActivity.EXTRA_USER, user)
            startActivity(moveToAddStoryActivity)
        }
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}