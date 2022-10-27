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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tiorisnanto.storyapp_risnanto73.MainActivity
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.register.RegisterActivity
import com.tiorisnanto.storyapp_risnanto73.activity.viewmodel.ViewModelFactory
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import com.tiorisnanto.storyapp_risnanto73.data.resultresponse.ResultResponse
import com.tiorisnanto.storyapp_risnanto73.dataStore
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityLoginBinding
import com.tiorisnanto.storyapp_risnanto73.helper.Helper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        buttonListener()
        playAnimation()
        setMyButtonEnable()
        editTextListener()


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
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
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
                binding.etPass.error = null
                setMessage(getString(R.string.login_failed) + ", $message")
                setPositiveButton(getString(R.string.continue_reading)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()
            }
        }
    }

    private fun buttonListener() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()

            loginViewModel.login(email, pass).observe(this) {
                when (it) {
                    is ResultResponse.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultResponse.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val user = UserModel(
                            it.data.name,
                            email,
                            pass,
                            it.data.userId,
                            it.data.token,
                            true
                        )
                        showAlertDialog(true, getString(R.string.login_success))

                        val userPref = UserPreferences.getInstance(dataStore)
                        lifecycleScope.launchWhenStarted {
                            userPref.saveUser(user)
                        }
                    }
                    is ResultResponse.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showAlertDialog(false, it.error)
                    }
                }
            }
        }
    }

}