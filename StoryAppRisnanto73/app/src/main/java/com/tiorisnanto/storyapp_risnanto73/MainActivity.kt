package com.tiorisnanto.storyapp_risnanto73

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.tiorisnanto.storyapp_risnanto73.activity.login.LoginActivity
import com.tiorisnanto.storyapp_risnanto73.activity.main.MainViewModel
import com.tiorisnanto.storyapp_risnanto73.activity.stories.ListStoriesActivity
import com.tiorisnanto.storyapp_risnanto73.activity.viewmodel.ViewModelFacotry
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityMainBinding

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var user: UserModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupViewModel()
        playAnimation()
        buttonListener()
    }

    private fun buttonListener() {
        binding.btnLisStory.setOnClickListener {
            val moveToListStoryActivity = Intent(this@MainActivity, ListStoriesActivity::class.java)
            moveToListStoryActivity.putExtra(ListStoriesActivity.EXTRA_USER, user)
            startActivity(moveToListStoryActivity)
        }
        binding.ivSetting?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.btnLogOut.setOnClickListener {
            mainViewModel.logout()
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.logout_success))
                setPositiveButton(getString(R.string.continue_reading)) { _, _ ->
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -38f, 38f).apply {
            duration = 600
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val logout = ObjectAnimator.ofFloat(binding.btnLogOut, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(name, message, logout)
            startDelay = 500
        }.start()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFacotry(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) {
            user = UserModel(
                it.name,
                it.email,
                it.password,
                it.userId,
                it.token,
                true
            )
            binding.nameTextView.text = getString(R.string.greeting, user.name)
        }
    }
}