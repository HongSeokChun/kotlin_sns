package com.android.example.hongseokchun.ui

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.databinding.PeedPostItemBinding
import com.android.example.hongseokchun.model.Posts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MyViewHolder(val binding: PeedPostItemBinding) : RecyclerView.ViewHolder(binding.root)

class PeedAdapter(itemList: List<Posts>) : RecyclerView.Adapter<MyViewHolder>() {
    lateinit var context: Context
    var itemList: List<Posts> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("post itemlist", itemList.toString())
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PeedPostItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cpItemList = itemList.reversed() //최신 게시물이 위로, 시간비교해서 정렬해야함

        val mainText = cpItemList[position].mainText
        val like = cpItemList[position].like
        val userName = cpItemList[position].postAdmin

        loadImage(holder.binding.detailviewitemImageviewContent, itemList[position].imageNames[0] , userName)

        holder.binding.detailviewitemProfileTextview.text = userName
        holder.binding.detailviewitemExplainTextview.text = mainText
        holder.binding.detailviewitemFavoritecounterTextview.text = like;
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
