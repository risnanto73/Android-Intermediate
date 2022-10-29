package com.tiorisnanto.storyapp_risnanto73.activity.stories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tiorisnanto.storyapp_risnanto73.activity.addstroies.AddStroiesActivity
import com.tiorisnanto.storyapp_risnanto73.activity.maps.MapsActivity
import com.tiorisnanto.storyapp_risnanto73.activity.viewmodel.ViewModelFactory
import com.tiorisnanto.storyapp_risnanto73.adapter.LoadingStatesAdapter
import com.tiorisnanto.storyapp_risnanto73.adapter.StoriesAdapter
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityListStoriesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListStoriesActivity : AppCompatActivity() {

    private var _binding: ActivityListStoriesBinding? = null
    private val binding get() = _binding

    private lateinit var user: UserModel
    private lateinit var adapter: StoriesAdapter
    private val viewModel: ListStoriesViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val EXTRA_USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListStoriesBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

//        //SupportActionBar
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)

        user = intent.getParcelableExtra(EXTRA_USER)!!

        initAdapter()
        initSwipeToRefresh()
        buttonListener()
    }

    private fun buttonListener() {
        binding?.ivAddStory?.setOnClickListener {
            val moveToAddStoriesActivity = Intent(this, AddStroiesActivity::class.java)
            moveToAddStoriesActivity.putExtra(AddStroiesActivity.EXTRA_USER, user)
            startActivity(moveToAddStoriesActivity)
        }
        binding?.ivMaps?.setOnClickListener {
            val moveToMapStory = Intent(this, MapsActivity::class.java)
            moveToMapStory.putExtra(AddStroiesActivity.EXTRA_USER, user)
            startActivity(moveToMapStory)
        }
        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.ivSetting?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding?.fabAddStories?.setOnClickListener {
            val moveToAddStoriesActivity = Intent(this, AddStroiesActivity::class.java)
            moveToAddStoriesActivity.putExtra(AddStroiesActivity.EXTRA_USER, user)
            startActivity(moveToAddStoriesActivity)
        }
    }

    // update data when swipe
    private fun initSwipeToRefresh() {
        binding?.swipeRefresh?.setOnRefreshListener { adapter.refresh() }
    }

    private fun initAdapter() {
        adapter = StoriesAdapter()
        binding?.rvStory?.adapter = adapter.withLoadStateHeaderAndFooter(
            footer = LoadingStatesAdapter { adapter.retry() },
            header = LoadingStatesAdapter { adapter.retry() }
        )
        binding?.rvStory?.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding?.rvStory?.setHasFixedSize(true)

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collect {
                binding?.swipeRefresh?.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding?.viewError?.root?.isVisible = loadStates.refresh is LoadState.Error
            }
            if (adapter.itemCount < 1) binding?.viewError?.root?.visibility = View.VISIBLE
            else binding?.viewError?.root?.visibility = View.GONE
        }

        viewModel.getStories(user.token).observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}