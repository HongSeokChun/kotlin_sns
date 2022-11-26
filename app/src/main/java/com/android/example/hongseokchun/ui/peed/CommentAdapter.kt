package com.android.example.hongseokchun.ui.peed

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.databinding.CommentItemBinding
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.Comment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//data class Student(val uid: Int, val name: String)
class CommentViewHolder(val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)
class CommentAdapter( val comments: ArrayList<Comment>)
        : RecyclerView.Adapter<CommentViewHolder>() {

        lateinit var context: Context
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CommentItemBinding.inflate(inflater, parent, false)
                context = parent.context
                return CommentViewHolder(binding)

        }

        override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
                val _comments = comments.sortedBy { it.uploadDate }
                val comment = _comments[position]

                holder.binding.commentviewitemTextviewComment.text = comment.comment
                holder.binding.commentviewitemTextviewProfile.text = comment.name;
                Log.d("comment",comment.comment)
                Log.d("name",comment.name)
                loadImage(holder.binding.commentviewitemImageviewProfile,comment.profileUrl)
        }

        override fun getItemCount(): Int {
                return comments.size
        }

        //image 불러오기
        fun loadImage(imageView: ImageView, url: String){
                context?.let {
                        Glide.with(it)
                                .load(url)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(imageView)
                }
        }


}
