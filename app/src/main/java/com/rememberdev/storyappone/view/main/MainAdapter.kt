package com.rememberdev.storyappone.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rememberdev.storyappone.databinding.ItemStoriesBinding
import com.rememberdev.storyappone.model.ListStoryItem
import com.rememberdev.storyappone.view.detail.DetailActivity

class MainAdapter(private val storiesList: List<ListStoryItem>) :
    RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ListViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.ListViewHolder, position: Int) {
        val data = storiesList[position]
        Glide.with(holder.itemView.context)
            .load(data.photoUrl)
            .apply(RequestOptions().override(300, 100))
            .into(holder.binding.ivItemPhoto)
        holder.binding.tvItemName.text = data.name
        holder.binding.tvItemDesciption.text = data.description
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("Story", data)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    it.context as Activity,
                    Pair(holder.binding.ivItemPhoto, "profile"),
                    Pair(holder.binding.tvItemName, "name"),
                    Pair(holder.binding.tvItemDesciption, "description")
                )
            it.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount() = storiesList.size


    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
}