package com.jun.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jun.data.Items
import com.jun.naverapi.databinding.RcvNaverapiBinding

class NewsAdapter():RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private lateinit var binding: RcvNaverapiBinding
    private val posts = mutableListOf<Items>()

    fun clear(){
        posts.clear()
        notifyDataSetChanged()
    }

    fun addPosts(posts: List<Items>) {
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(val binding: RcvNaverapiBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(data : Items){
            binding.news=data
            // 바인딩을 즉시 실행해야 할 경우에 사용
            binding.executePendingBindings()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        binding=RcvNaverapiBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(posts[position])
    }
    override fun getItemCount()= posts.size
}



