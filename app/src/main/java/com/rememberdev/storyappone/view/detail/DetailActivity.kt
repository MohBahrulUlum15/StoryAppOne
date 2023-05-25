@file:Suppress("DEPRECATION")

package com.rememberdev.storyappone.view.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rememberdev.storyappone.databinding.ActivityDetailBinding
import com.rememberdev.storyappone.model.ListStoryItem

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewDetail()
        setupDataDetail()
    }

    private fun setupDataDetail() {
        val dataDetail = intent.getParcelableExtra<ListStoryItem>("Story") as ListStoryItem
        dataDetail.name?.let { Log.d("Data Detail", it) }
        Glide.with(applicationContext)
            .load(dataDetail.photoUrl)
            .apply(RequestOptions().override(350, 150))
            .into(binding.ivItemPhoto)
        binding.tvItemName.text = dataDetail.name
        binding.tvItemDesciption.text = dataDetail.description
    }

    private fun setupViewDetail() {
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