package com.rememberdev.storyappone.view.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rememberdev.storyappone.ViewModelFactory
import com.rememberdev.storyappone.databinding.ActivityMainBinding
import com.rememberdev.storyappone.model.ListStoryItem
import com.rememberdev.storyappone.model.UserPreferences
import com.rememberdev.storyappone.view.onboarding.OnBoardingActivity
import com.rememberdev.storyappone.view.upload.UploadActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var rvStories: RecyclerView
    private val listStories = ArrayList<ListStoryItem>()

    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewMain()
        setupViewModelMain()
        setupActionMain()

        rvStories = binding.rvStories
        rvStories.setHasFixedSize(true)
        showListStories(listStories)

        mainViewModel.photoList.observe(this) {
            showListStories(it)
        }

        showLoading(true)
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showListStories(listStories: List<ListStoryItem>) {
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvStories.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvStories.layoutManager = LinearLayoutManager(this)
        }

        val adapter = MainAdapter(listStories)
        Log.d("RecyclerView_Main", adapter.itemCount.toString())
        rvStories.adapter = adapter
    }

    private fun setupActionMain() {
        binding.actionLogout.setOnClickListener {
            mainViewModel.logoutUser()
        }

        binding.actionUpload.setOnClickListener{
            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
    }

    private fun setupViewModelMain() {
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]
        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            } else {
                token = user.token
                mainViewModel.getStoriesList(token)
            }
        }
    }

    private fun setupViewMain() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(it: Boolean?) {
        binding.progressBar.visibility = if (it == true) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}