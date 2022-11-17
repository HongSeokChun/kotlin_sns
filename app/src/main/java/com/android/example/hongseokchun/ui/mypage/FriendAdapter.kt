package com.android.example.hongseokchun.ui.mypage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.databinding.FriendListViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FriendAdapter(itemList: ArrayList<HashMap<String,String>>)
    : RecyclerView.Adapter<FriendAdapter.ViewHolder>(){
    lateinit var context: Context

    var itemList: ArrayList<HashMap<String,String>> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("friend itemlist",itemList.toString())
        }

    inner class ViewHolder(itemViewBinding: FriendListViewBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        val layout : ConstraintLayout = itemViewBinding.layoutFriend
        val friendImg: ImageView = itemViewBinding.imageView
        val friendName: TextView = itemViewBinding.friendName
        val deleteBtn: Button = itemViewBinding.btnDelete
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        return ViewHolder(
            FriendListViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friendName.text =itemList[position].get("name")
        itemList[position].get("profileImg")?.let { loadImage(holder.friendImg, it) }

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.deleteBtn.setOnClickListener {
            Log.d("clickk adapter","눌림")
            itemClickListener?.onClick(it, position)
        }
    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private var itemClickListener : OnItemClickListener? = null

    // firebase storage에서 이미지 불러오기
    fun loadImage(imageView: ImageView, fileName: String){
        val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
        val storageRef: StorageReference = storage.reference
        storageRef.child("userProfileImage/$fileName").downloadUrl.addOnCompleteListener { task ->
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

    override fun getItemCount(): Int = itemList.size
}