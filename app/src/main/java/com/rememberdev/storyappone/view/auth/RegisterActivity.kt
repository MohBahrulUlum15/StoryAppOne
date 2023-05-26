package com.rememberdev.storyappone.view.auth

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.rememberdev.storyappone.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewRegister()
        setupActionRegister()
    }

    private fun setupViewRegister() {
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

    private fun setupActionRegister() {
        binding.actionRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            when {
                name.isEmpty() -> {
                    Toast.makeText(this, "Please input Your Name", Toast.LENGTH_SHORT).show()
                }
                email.isEmpty() -> {
                    Toast.makeText(this, "Please input Email", Toast.LENGTH_SHORT).show()
                }
                email.isNotEmpty() && !email.contains("@") -> {
                    Toast.makeText(
                        this,
                        "invalid Email! please try input Email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Please input Password", Toast.LENGTH_SHORT).show()
                }
                password.length < 8 -> {
                    Toast.makeText(
                        this,
                        "Password must be longer than 8 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    registerViewModel.userRegister(name, email, password)
                    registerViewModel.successRegister.observe(this) {
                        if (it) {
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeah!")
                                setMessage("account created successfully")
                                setPositiveButton("Continue") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                        } else {
                            AlertDialog.Builder(this).apply {
                                setTitle("Oops!")
                                setMessage("Failed to create account")
                                setPositiveButton("Try again") { _, _ ->
                                }
                                create()
                                show()
                            }
                        }

                    }
                }
            }
        }
    }
}