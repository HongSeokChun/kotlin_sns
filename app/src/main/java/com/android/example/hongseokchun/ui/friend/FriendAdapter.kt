package com.android.example.hongseokchun.ui.friend

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
import androidx.navigation.Navigator
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.databinding.FriendListViewBinding
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.MyViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
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
    @SuppressLint("RestrictedApi", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friendName.text =itemList[position].get("name")
        itemList[position].get("profile_img")?.let { loadImage(holder.friendImg, it) }
        itemList[position].get("name")?.let { setFollowingBtn(it,holder) }

        // 친구 프로필 사진 클릭시
        holder.friendImg.setOnClickListener {
            itemList[position].get("email")?.let { it1 -> prefs.setString("watchUser", it1) }
            itemClickListener?.onClick("", position)
        }

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.deleteBtn.setOnClickListener {
            Log.d("clickk adapter","눌림")
            if(holder.deleteBtn.text == "팔로잉") {
                holder.deleteBtn.text = "팔로우"
                holder.deleteBtn.setBackgroundResource(R.drawable.follow_button)
            }
            else {
                holder.deleteBtn.text = "팔로잉"
                holder.deleteBtn.setBackgroundResource(R.drawable.following_button)
            }
            itemList[position].get("email")?.let { it1 -> prefs.setString("watchUser", it1) }
            itemList[position].get("email")?.let { it1 -> Log.d("email:" , it1) }
            itemClickListener?.onClick(holder.deleteBtn.text.toString(), position)

        }

    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(btn:String, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private var itemClickListener : OnItemClickListener? = null

    // firebase storage에서 이미지 불러오기
    fun loadImage(imageView: ImageView, url: String){
        Glide.with(context)
            .load(url)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    // 팔로우버튼 모양 정하기
    fun setFollowingBtn(name:String, holder:ViewHolder){
        var followingList : String = ""
        val db = Firebase.firestore

        db.collection("users").document(prefs.getString("email","null")).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                if (data != null) {
                    followingList = data.following.toString()
                    if(name in followingList){
                        holder.deleteBtn.text = "팔로잉"
                        holder.deleteBtn.setBackgroundResource(R.drawable.following_button)

                    }else{
                        holder.deleteBtn.text = "팔로우"
                        holder.deleteBtn.setBackgroundResource(R.drawable.follow_button)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }

    override fun getItemCount(): Int = itemList.size
}