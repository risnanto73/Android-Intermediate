package com.tiorisnanto.storyapp_risnanto73.activity.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.tiorisnanto.storyapp_risnanto73.MainActivity
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.login.LoginActivity
import com.tiorisnanto.storyapp_risnanto73.activity.viewmodel.ViewModelFacotry
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityMainRouteBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MainRouteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainRouteBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main_route)
        setupViewModel()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFacotry(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) {
            if (it.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}