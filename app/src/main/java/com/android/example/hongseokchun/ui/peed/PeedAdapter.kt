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
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.Notify
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.peed.PeedFragmentDirections
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.common.net.InetAddresses.decrement
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
        cpItemList = itemList.sortedByDescending { it.uploadDate }// ?????? ???????????? ??????,
        var liking = false //?????? ????????? ???????????? ??????
        var firstLiking = true //?????? ????????? ????????? ?????????
        val mainText = cpItemList[position].mainText
        val like = "????????? " + cpItemList[position].like.toString() + "???"
        val postAdminEmail = cpItemList[position].postAdmin
        val uploadDate = cpItemList[position].uploadDate.substring(0,13)
        //image ????????????
        loadImage(
            holder.binding.detailviewitemProfileImage,
            cpItemList[position].postAdminProfile
        )
        loadImage(
            holder.binding.detailviewitemImageviewContent,
            cpItemList[position].imageNames[0].toString()
        )

        //????????? ??????, ??????, ????????? ???, ??????????????? ??????
        holder.binding.detailviewitemProfileTextview.text = postAdminEmail
        holder.binding.detailviewitemExplainTextview.text = mainText
        holder.binding.detailviewitemFavoritecounterTextview.text = like
        holder.binding.detailviewitemExplainTextview3.text = uploadDate
        holder.binding.detailviewitemCommentCountTextview.text = "?????? "+cpItemList[position].commentCount.toString()+ "??? ?????? ??????"

        //Post?????? ??? ????????? ?????? ????????????????????? ??????????????? ????????? ??????
        for (likeMemeber in cpItemList[position].likes) {
            if (likeMemeber == currentUserEamil) {
                liking = true
                break
            } else {
                liking = false
            }
        }

        //?????? ????????? ????????? ?????? ????????? ????????? ??????
        if (liking == true) {
            holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }

        // ?????? ????????? ?????? ?????????
        holder.binding.detailviewitemProfileImage.setOnClickListener {
            if(postAdminEmail != currentUserEamil) {
                prefs.setString("watchUser", postAdminEmail)
                itemClickListener?.onClick("", position)
            }
        }

        // ??????????????? ?????????
        holder.binding.detailviewitemFavoritecounterTextview.setOnClickListener {
            //navController.navigate(R.id.action_peedFragment_to_commentFragment)
            var usernameANDpostid = listOf<String>()
            CoroutineScope(Dispatchers.IO).launch {
                runBlocking {
                    db.collection("users").get() //?????? ??????
                        .addOnSuccessListener { userdocuments ->
                            for (userdocument in userdocuments) {
                                Log.d("document.id", userdocument.id)
                                db.collection("users").document(userdocument.id).collection("Post") //?????? ?????? ???????????????
                                    .whereEqualTo("uploadDate", cpItemList[position].uploadDate)//uploadDate??? ??????, ??? ?????? position ?????????
                                    .get()//????????????
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) { //for-in ????????? ????????? ?????????
                                            Log.d("postId2", document.id)
                                            usernameANDpostid = listOf(userdocument.id, document.id) //userEmail, postid
                                            Log.d("postId3", usernameANDpostid.toString())
                                            val action =
                                                PeedFragmentDirections.actionPeedFragmentToFindFavoriteUserFragment(
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

        //?????? ???????????? ?????????
        holder.binding.detailviewitemCommentCountTextview.setOnClickListener {
            //navController.navigate(R.id.action_peedFragment_to_commentFragment)
            var usernameANDpostid = listOf<String>()
            CoroutineScope(Dispatchers.IO).launch {
                runBlocking {
                    db.collection("users").get() //?????? ??????
                        .addOnSuccessListener { userdocuments ->
                            for (userdocument in userdocuments) {
                                Log.d("document.id", userdocument.id)
                                db.collection("users").document(userdocument.id).collection("Post") //?????? ?????? ???????????????
                                    .whereEqualTo("uploadDate", cpItemList[position].uploadDate)//uploadDate??? ??????, ??? ?????? position ?????????
                                    .get()//????????????
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) { //for-in ????????? ????????? ?????????
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

        //???????????? ?????????
        holder.binding.detailviewitemCommentImageview.setOnClickListener {
            //navController.navigate(R.id.action_peedFragment_to_commentFragment)
            var usernameANDpostid = listOf<String>()
            CoroutineScope(Dispatchers.IO).launch {
                runBlocking {
                    db.collection("users").get() //?????? ??????
                        .addOnSuccessListener { userdocuments ->
                            for (userdocument in userdocuments) {
                                Log.d("document.id", userdocument.id)
                                db.collection("users").document(userdocument.id).collection("Post") //?????? ?????? ???????????????
                                    .whereEqualTo("uploadDate", cpItemList[position].uploadDate)//uploadDate??? ??????, ??? ?????? position ?????????
                                    .get()//????????????
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) { //for-in ????????? ????????? ?????????
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

        //????????? ?????? ?????????
        holder.binding.detailviewitemFavoriteImageview.setOnClickListener {
            if (liking == false) {//???????????? ????????? ?????????

                //like +1
                incrementLike(position)

                //????????? ?????? true
                liking = true



                //????????? ??????
                holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_24)

                //?????? ????????? ????????? ????????????
                holder.binding.detailviewitemFavoritecounterTextview.text =
                    "????????? " + (cpItemList[position].like + 1).toString() + "???" //?????? ????????? ?????? ????????????

                //?????? ??????
                //????????? ?????? 1??? ??????????????? ?????? ??????
                if (firstLiking == true) {
                    //?????? ??????

                    //???????????? ????????? ????????? ??? ????????? ????????? ????????? ???????????? ?????????
                    val alarmDTO = AlarmDTO()
                    alarmDTO.destinationUid =postAdminEmail
                    alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
                    alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
                    alarmDTO.kind = 0
                    alarmDTO.message ="${prefs.getString("name","")}?????? ???????????? ???????????????."
                    alarmDTO.timestamp = System.currentTimeMillis()


                    FirebaseFirestore.getInstance().collection("users").document(postAdminEmail)
                        .collection("Alarm").document().set(alarmDTO)
                }
            } else {// ???????????? ???????????????
                //????????? ?????? fasle
                liking = false

                //??? ?????? ??????
                holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                //like -1
                decrementLike(position)

                //?????? ????????? ????????? ????????????
                holder.binding.detailviewitemFavoritecounterTextview.text =
                    "????????? " + (cpItemList[position].like).toString() + "???"

            }
        }
        holder.binding.detailviewitemFavoritecounterTextview.text = like.toString();
    }

    //image ????????????
    // firebase storage?????? ????????? ????????????
    fun loadImage(imageView: ImageView, url: String){
        Glide.with(context)
            .load(url)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    // (2) ????????? ???????????????
    interface OnItemClickListener {
        fun onClick(btn:String, position: Int)
    }
    // (3) ???????????? ?????? ??? ????????? ??????
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener??? ????????? ?????? ??????
    private var itemClickListener : OnItemClickListener? = null


    override fun getItemCount(): Int {
        return itemList.size
    }

    fun notifyPostMessage(name: String): String {
        return "${name}?????? ???????????? ???????????? ???????????????."
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

