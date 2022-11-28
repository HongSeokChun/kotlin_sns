package com.android.example.hongseokchun.ui.peed

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.MyApplication
import com.android.example.hongseokchun.databinding.FragmentNotificationBinding
import com.android.example.hongseokchun.databinding.FriendListViewBinding
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.friend.FriendAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class NotificationAdapter (itemList: ArrayList<AlarmDTO>)
    : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){
    lateinit var context: Context
    val db = Firebase.firestore


    var itemList: ArrayList<AlarmDTO> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("friend itemlist",itemList.toString())
        }

    inner class ViewHolder(itemViewBinding: FragmentNotificationBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        val profileImg: ImageView = itemViewBinding.imageView
        val message: TextView = itemViewBinding.tvNotification
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        return ViewHolder(
            FragmentNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        loadImage(holder.profileImg, itemList[position].)
        itemList[position].userId?.let { getProfileImage(it,holder) }
        holder.message.text = itemList[position].message
    }
    override fun getItemCount(): Int = itemList.size

    // firebase storage에서 이미지 불러오기
    fun loadImage(imageView: ImageView, url: String){
        Glide.with(context)
            .load(url)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    fun getProfileImage(email:String, holder:ViewHolder){
        db.collection("users").document(email).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                val profileImg = data?.profile_img.toString()
                loadImage(holder.profileImg,profileImg)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }
}