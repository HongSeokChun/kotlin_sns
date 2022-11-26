package com.android.example.hongseokchun.ui

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.MyApplication
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.databinding.PeedPostItemBinding
import com.android.example.hongseokchun.model.Notify
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.peed.PeedFragmentDirections
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.common.net.InetAddresses.decrement
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class MyViewHolder(val binding: PeedPostItemBinding) : RecyclerView.ViewHolder(binding.root)

class PeedAdapter(itemList: List<Posts>) : RecyclerView.Adapter<MyViewHolder>() {
    lateinit var context: Context
    private val db = Firebase.firestore
    private var cpItemList = listOf<Posts>()
    private var currentUserEamil: String? = null

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
        currentUserEamil = Firebase.auth.currentUser?.email
        cpItemList = itemList.sortedByDescending { it.uploadDate }// 최신 게시물이 위로,
        var liking = false //현재 좋아요 눌렀는지 여부
        var firstLiking = true //처음 좋아요 누르는 것인지
        val mainText = cpItemList[position].mainText
        val like = "좋아요 " + cpItemList[position].like.toString() + "개"
        val postAdminEmail = cpItemList[position].postAdmin
        val uploadDate = cpItemList[position].uploadDate.substring(0,13)
        //image 불러오기
        loadProfileImage(
            holder.binding.detailviewitemProfileImage,
            cpItemList[position].postAdminProfile
        )
        loadImage(
            holder.binding.detailviewitemImageviewContent,
            "postImage",
            cpItemList[position].imageNames[0],
            postAdminEmail
        )

        //게시믈 이름, 설명, 좋아요 수, 업로드시간 설정
        holder.binding.detailviewitemProfileTextview.text = postAdminEmail
        holder.binding.detailviewitemExplainTextview.text = mainText
        holder.binding.detailviewitemFavoritecounterTextview.text = like
        holder.binding.detailviewitemExplainTextview3.text = uploadDate
        holder.binding.detailviewitemCommentCountTextview.text = "댓글 "+cpItemList[position].commentCount.toString()+ "개 모두 보기"

        //Post속성 중 좋아요 누른 멤버리스트에서 현재유저가 있는지 확인
        for (likeMemeber in cpItemList[position].likes) {
            if (likeMemeber == currentUserEamil) {
                liking = true
                break
            } else {
                liking = false
            }
        }

        //현재 좋아요 여부에 따라 좋아요 이미지 구성
        if (liking == true) {
            holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }

        //댓글 모두보기 누르면
        holder.binding.detailviewitemCommentCountTextview.setOnClickListener {
            //navController.navigate(R.id.action_peedFragment_to_commentFragment)
            var usernameANDpostid = listOf<String>()
            CoroutineScope(Dispatchers.IO).launch {
                runBlocking {
                    db.collection("users").get() //모든 유저
                        .addOnSuccessListener { userdocuments ->
                            for (userdocument in userdocuments) {
                                Log.d("document.id", userdocument.id)
                                db.collection("users").document(userdocument.id).collection("Post") //모든 유저 포스트에서
                                    .whereEqualTo("uploadDate", cpItemList[position].uploadDate)//uploadDate가 같은, 즉 해당 position 포스트
                                    .get()//가져와서
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) { //for-in 이지만 포스트 하나임
                                            Log.d("postId2", document.id)
                                            usernameANDpostid = listOf(userdocument.id, document.id) //userEmail, postid
                                            Log.d("postId3", usernameANDpostid.toString())
                                            val action =
                                                PeedFragmentDirections.actionPeedFragmentToCommentFragment(
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

        //댓글버튼 누르면
        holder.binding.detailviewitemCommentImageview.setOnClickListener {
            //navController.navigate(R.id.action_peedFragment_to_commentFragment)
            var usernameANDpostid = listOf<String>()
            CoroutineScope(Dispatchers.IO).launch {
                runBlocking {
                    db.collection("users").get() //모든 유저
                        .addOnSuccessListener { userdocuments ->
                            for (userdocument in userdocuments) {
                                Log.d("document.id", userdocument.id)
                                db.collection("users").document(userdocument.id).collection("Post") //모든 유저 포스트에서
                                    .whereEqualTo("uploadDate", cpItemList[position].uploadDate)//uploadDate가 같은, 즉 해당 position 포스트
                                    .get()//가져와서
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) { //for-in 이지만 포스트 하나임
                                            Log.d("postId2", document.id)
                                            usernameANDpostid = listOf(userdocument.id, document.id) //userEmail, postid
                                            Log.d("postId3", usernameANDpostid.toString())
                                            val action =
                                                PeedFragmentDirections.actionPeedFragmentToCommentFragment(
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

        //좋아요 버튼 누르면
        holder.binding.detailviewitemFavoriteImageview.setOnClickListener {
            if (liking == false) {//좋아요가 안눌려 있으면

                //like +1
                incrementLike(position)

                //좋아요 상태 true
                liking = true

                //이미지 변경
                holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_24)

                //피드 좋아요 텍스트 업데이트
                holder.binding.detailviewitemFavoritecounterTextview.text =
                    "좋아요 " + (cpItemList[position].like + 1).toString() + "개" //피드 좋아요 개수 없데이트

                //알림 생성
                //좋아요 최초 1회 눌렀을때만 알림 생성
                if (firstLiking == true) {
                    //알림 생성

                    var notificationPost =
                        Notify(/*  prefernce 써보기 */
                            "mySharedPreferences.prefs.getSt",
                            "MyApplication.prefs.getString",
                            "notifyPostMessage(MyApplication.prefs.getString"
                        )
                    Log.d("NotfiyLog",notificationPost.toString())
                    db.collection("users").document(cpItemList[position].postAdmin)
                        .collection("Notification")
                        .document().set(notificationPost)
                }
            } else {// 좋아요가 눌려있으면
                //좋아요 상태 fasle
                liking = false

                //빈 하트 변경
                holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                //like -1
                decrementLike(position)

                //피드 좋아요 텍스트 업데이트
                holder.binding.detailviewitemFavoritecounterTextview.text =
                    "좋아요 " + (cpItemList[position].like).toString() + "개"

            }
        }
        holder.binding.detailviewitemFavoritecounterTextview.text = like.toString();
    }

    //image 불러오기
    fun loadImage(imageView: ImageView, folderName: String, fileName: String, userName: String) {
        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
        val storageRef: StorageReference = storage.reference
        storageRef.child("${folderName}/${userName}/${fileName}").downloadUrl.addOnCompleteListener { task ->
            Log.d("Imagepath = ", "${folderName}/${userName}/${fileName}")
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

    fun loadProfileImage(imageView: ImageView, fileName: String) {
        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
        val storageRef: StorageReference = storage.reference
        if (fileName != null) {
            storageRef.child("userProfileImage/${fileName}").downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("userProfileImage", "userProfileImage2/${fileName}")
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
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun notifyPostMessage(name: String): String {
        return "${name}님이 회원님의 게시글을 좋아합니다."
    }


    fun incrementLike(position: Int) {
        Log.d("cpItemList[position].uploadDat", cpItemList[position].uploadDate.toString())
        db.collection("users").get()
            .addOnSuccessListener { userdocuments ->
                for (userdocument in userdocuments) {
                    Log.d("document.id", userdocument.id)
                    db.collection("users").document(userdocument.id).collection("Post")
                        .whereEqualTo("uploadDate", cpItemList[position].uploadDate)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                Log.d("postId2", document.id)
                                db.collection("users").document(userdocument.id).collection("Post")
                                    .document(document.id)
                                    .update(
                                        "like",
                                        FieldValue.increment(1),
                                        "likes",
                                        FieldValue.arrayUnion(currentUserEamil)
                                    )
                            }
                        }
                }
            }
    }

    fun decrementLike(position: Int) {
        Log.d("cpItemList[position].uploadDat", cpItemList[position].uploadDate.toString())
        db.collection("users").get()
            .addOnSuccessListener { userdocuments ->
                for (userdocument in userdocuments) {
                    Log.d("document.id", userdocument.id)
                    db.collection("users").document(userdocument.id).collection("Post")
                        .whereEqualTo("uploadDate", cpItemList[position].uploadDate)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                Log.d("postId2", document.id)
                                db.collection("users").document(userdocument.id).collection("Post")
                                    .document(document.id)
                                    .update(
                                        "like",
                                        FieldValue.increment(-1),
                                        "likes",
                                        FieldValue.arrayRemove(currentUserEamil)
                                    )
                            }
                        }
                }
            }
    }

    suspend fun getPostid(position: Int): String {
        var postId = ""
        return try {
            db.collection("users").get()
                .addOnSuccessListener { userdocuments ->
                    for (userdocument in userdocuments) {
                        Log.d("document.id", userdocument.id)
                        db.collection("users").document(userdocument.id).collection("Post")
                            .whereEqualTo("uploadDate", itemList[position].uploadDate)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    Log.d("postId2", document.id)
                                    postId = document.id
                                    Log.d("postId3", postId)

                                }
                            }
                    }
                }.await()
            postId

        } catch (e: FirebaseException) {
            Log.d("getPostExceoption", "")
            ""
        }
    }

}

