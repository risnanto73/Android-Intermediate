package com.tiorisnanto.storyapp_risnanto73.activity.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.tiorisnanto.storyapp_risnanto73.MainActivity
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.register.RegisterActivity
import com.tiorisnanto.storyapp_risnanto73.activity.viewmodel.ViewModelFacotry
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import com.tiorisnanto.storyapp_risnanto73.dataStore
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityLoginBinding
import com.tiorisnanto.storyapp_risnanto73.helper.Helper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setMyButtonEnable()
        editTextListener()
        buttonListener()
        showLoading()
        playAnimation()

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 600
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 1f).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.etSignIn, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together, tvLogin)
            start()
        }
    }

    private fun showLoading() {
        loginViewModel.isLoading.observe(this) {
            binding.apply {
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        }
    }

    private fun buttonListener() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()

            loginViewModel.login(email, pass, object : Helper.ApiCallbackString {
                override fun onResponse(success: Boolean,message: String) {
                    showAlertDialog(success, message)
                }
            })
        }

        binding.ivSetting?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.login_success))
                setPositiveButton(getString(R.string.continue_reading)) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.login_failed) +", $message")
                setPositiveButton(getString(R.string.continue_reading)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()

            }
        }
    }

    private fun editTextListener() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.etPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.etSignIn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun setMyButtonEnable() {
        val resultPass = binding.etPass.text
        val resultEmail = binding.etEmail.text

        binding.btnSignIn.isEnabled = resultPass != null && resultEmail != null &&
                binding.etPass.text.toString().length >= 6 &&
                Helper.isEmailValid(binding.etEmail.text.toString())
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFacotry(UserPreferences.getInstance(dataStore))
        ).get(LoginViewModel::class.java)
    }
}