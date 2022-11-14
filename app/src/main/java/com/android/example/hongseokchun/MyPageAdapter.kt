package com.android.example.hongseokchun

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.databinding.MyPageItemBinding

//data class Student(val uid: Int, val name: String)
//data class Post(val uid: Int,val time: String, val like: String, val comment: String)

class MyPageViewHolder(val binding: MyPageItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyPageAdapter(private val context: Context, private val posts: MutableList<Post>) : RecyclerView.Adapter<MyPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MyPageItemBinding.inflate(inflater, parent, false)
        return MyPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageViewHolder, position: Int) {
        val post = posts[position]

        holder.binding.myPagePostImage.setImageResource(R.drawable.sample)
        holder.binding.myPagePostTitle.text = "MY POST"
        holder.binding.myPagePostComment.text = "댓글 12개"
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
