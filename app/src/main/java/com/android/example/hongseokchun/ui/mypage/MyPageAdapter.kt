package com.android.example.hongseokchun.ui.mypage

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.databinding.MyPageItemBinding
import com.android.example.hongseokchun.model.Posts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class MyPageViewHolder(val binding: MyPageItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyPageAdapter(itemList: List<Posts>) : RecyclerView.Adapter<MyPageViewHolder>() {
    lateinit var context: Context
    private val db = Firebase.firestore
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
        val countOfComments = cpItemList[position].commentCount

        loadImage(
            holder.binding.myPagePostImage,
            cpItemList[position].imageNames[0]
        )

        holder.binding.myPagePostTitle.text = "좋아요 ${countOfLikes}개"
        holder.binding.myPagePostComment.text = "댓글 ${countOfComments}개"

        holder.binding.myPagePostImage.setOnClickListener {
            //navController.navigate(R.id.action_peedFragment_to_commentFragment)
            var usernameANDpostid = listOf<String>()
            CoroutineScope(Dispatchers.IO).launch {
                runBlocking {
                    db.collection("users").get() //모든 유저
                        .addOnSuccessListener { userdocuments ->
                            for (userdocument in userdocuments) {
                                Log.d("document.id", userdocument.id)
                                db.collection("users").document(userdocument.id)
                                    .collection("Post") //모든 유저 포스트에서
                                    .whereEqualTo(
                                        "uploadDate",
                                        cpItemList[position].uploadDate
                                    )//uploadDate가 같은, 즉 해당 position 포스트
                                    .get()//가져와서
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) { //for-in 이지만 포스트 하나임
                                            Log.d("postId2", document.id)
                                            usernameANDpostid = listOf(
                                                userdocument.id,
                                                document.id
                                            ) //userEmail, postid
                                            Log.d("postId3", usernameANDpostid.toString())
                                            val action =
                                                MyPageFragmentDirections.actionMyPageFragmentToOnePostFragment(
                                                    usernameANDpostid.toTypedArray()
                                                )
                                            it.findNavController().navigate(action)
                                        }
                                    }
                            }
                        }.await()
                    Log.d("getPostId", usernameANDpostid.toString())
                }
            }


        }
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

    override fun getItemCount(): Int {
        return itemList.size
    }
}

