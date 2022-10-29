package com.tiorisnanto.storyapp_risnanto73.activity.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tiorisnanto.storyapp_risnanto73.MainActivity
import com.tiorisnanto.storyapp_risnanto73.activity.login.LoginActivity
import com.tiorisnanto.storyapp_risnanto73.activity.viewmodel.ViewModelUserFactory
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityMainRouteBinding
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MainRouteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainRouteBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelUserFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        lifecycleScope.launchWhenCreated {
            launch {
                mainViewModel.getUser().collect {
                    if(it.isLogin){
                        gotoMainActivity(true)
                    }
                    else gotoMainActivity(false)
                }
            }
        }
    }

    private fun gotoMainActivity(boolean: Boolean){
        if (boolean) {
            startActivity(
                Intent(this, MainActivity::class.java),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle()
            )
            finish()
        } else {
            startActivity(
                Intent(this, LoginActivity::class.java),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle()
            )
            finish()
        }
    }
}