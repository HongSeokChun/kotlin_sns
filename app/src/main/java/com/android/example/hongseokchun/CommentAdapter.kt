package com.example.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.CommentItemBinding
import com.example.recyclerview.databinding.MyPageItemBinding

//data class Student(val uid: Int, val name: String)
//data class Post(val uid: Int,val time: String, val like: String, val comment: String)

class CommentViewHolder(val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)

class CommentAdapter(private val context: Context, private val comments: MutableList<Comment>) : RecyclerView.Adapter<CommentViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CommentItemBinding.inflate(inflater, parent, false)
                return CommentViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
                val comment = comments[position]

                holder.binding.commentviewitemTextviewComment.text = comment.message
                holder.binding.commentviewitemTextviewProfile.text = comment.User.name;
                holder.binding.commentviewitemImageviewProfile.setImageResource(R.drawable.sample);
        }

        override fun getItemCount(): Int {
                return comments.size
        }
}
