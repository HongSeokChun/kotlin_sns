package com.android.example.hongseokchun.ui.mypage

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.databinding.MyPageItemBinding
import com.android.example.hongseokchun.model.Posts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyPageViewHolder(val binding: MyPageItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyPageAdapter(itemList: List<Posts>) : RecyclerView.Adapter<MyPageViewHolder>() {
    lateinit var context: Context
    var itemList: List<Posts> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("postitemlist", itemList.toString())
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MyPageItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return MyPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageViewHolder, position: Int) {
        val cpItemList = itemList.reversed() //최신 게시물이 위로
        val countOfLikes = cpItemList[position].like
        val userName = cpItemList[position].postAdmin
        val countOfComments = cpItemList[position].comments.size

        loadImage(holder.binding.myPagePostImage, itemList[position].imageNames[0] , userName)

        holder.binding.myPagePostTitle.text = "좋아요 ${countOfLikes}개"
        holder.binding.myPagePostComment.text = "댓글 ${countOfComments}개"
    }

    //image 불러오기
    fun loadImage(imageView: ImageView, fileName: String, userName: String) {
        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
        val storageRef: StorageReference = storage.reference
        storageRef.child("postImage/${userName}/${fileName}.jpg").downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView)
            }
        }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)

            }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
