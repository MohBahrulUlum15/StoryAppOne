package com.rememberdev.storyappone.view.auth

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.rememberdev.storyappone.ViewModelFactory
import com.rememberdev.storyappone.databinding.ActivityLoginBinding
import com.rememberdev.storyappone.model.UserPreferences
import com.rememberdev.storyappone.view.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewLogin()
        setupViewModelLogin()
        setupActionLogin()

        showLoading(false)
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(it: Boolean?) {
        binding.progressBar.visibility = if (it == true) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setupActionLogin() {
        binding.actionLogin.setOnClickListener{
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            when{
                email.isEmpty() -> {
                    Toast.makeText(this, "Please input Email", Toast.LENGTH_SHORT).show()
                }
                email.isNotEmpty() && !email.contains("@") -> {
                    Toast.makeText(this, "invalid Email! please try input Email", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Please input Password", Toast.LENGTH_SHORT).show()
                }
                password.length < 8 -> {
                    Toast.makeText(this, "Password must be longer than 8 characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    loginViewModel.userLogin(email, password)
                    loginViewModel.successLogin.observe(this){
                        if (it){
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else if (!it){
                            AlertDialog.Builder(this).apply {
                                setTitle("Oops!")
                                setMessage("Email or Password is wrong")
                                setPositiveButton("Try again") {_, _ ->}
                                create()
                                show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupViewModelLogin() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupViewLogin() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}