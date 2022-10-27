package com.tiorisnanto.storyapp_risnanto73.activity.register

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
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.login.LoginActivity
import com.tiorisnanto.storyapp_risnanto73.activity.viewmodel.ViewModelFactory
import com.tiorisnanto.storyapp_risnanto73.data.resultresponse.ResultResponse
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityRegisterBinding
import com.tiorisnanto.storyapp_risnanto73.helper.Helper

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private val registerViewModel : RegisterViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMyButtonEnable()
        editTextListener()
        buttonListener()
        playAnimation()

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 600
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val register = ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.etSignin, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(register)
        }

        AnimatorSet().apply {
            playSequentially(together, name,email, pass, btnRegister, btnLogin)
            start()
        }
    }

    private fun buttonListener() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()

            registerViewModel.register(name, email, password).observe(this){
                when (it) {
                    is ResultResponse.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultResponse.Success -> {
                        binding.progressBar.visibility = View.GONE
                        showAlertDialog(true, getString(R.string.register_success))
                    }
                    is ResultResponse.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showAlertDialog(false, it.error)
                    }
                }
            }
        }
        binding.ivSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.register_success))
                setPositiveButton(getString(R.string.continue_reading)) { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
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
                setMessage(getString(R.string.register_failed)+", $message")
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

        binding.etSignin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun setMyButtonEnable() {
        binding.btnRegister.isEnabled =
            binding.etEmail.text.toString().isNotEmpty() &&
                    binding.etPass.text.toString().isNotEmpty() &&
                    binding.etPass.text.toString().length >= 6 &&
                    Helper.isEmailValid(binding.etEmail.text.toString())
    }
}