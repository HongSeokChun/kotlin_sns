package com.android.example.hongseokchun.ui.mypage

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.MyApplication
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentOnePostBinding
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.Notify
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.ui.peed.CommentFragmentArgs
import com.android.example.hongseokchun.ui.peed.PeedFragmentDirections
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class OnePostFragment : BaseFragment<FragmentOnePostBinding>(R.layout.fragment_one_post) {
    val args: OnePostFragmentArgs by navArgs()
    val db = Firebase.firestore
    lateinit var mainActivity: MainActivity
    var currentUserEamil = Firebase.auth.currentUser?.email
    var liking = false //현재 좋아요 눌렀는지 여부
    var firstLiking = true //처음 좋아요 누르는 것인지
    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("view")
    }

    override fun initDataBinding() {
        super.initDataBinding()

        val userName = args.nameANDpostId2[0]
        val postid = args.nameANDpostId2[1]


       db.collection("users").document(userName).collection("Post").document(postid)
           .get()
           .addOnSuccessListener {
               val data = it.toObject<Posts>()
               binding.apply {
                   loadProfileImage(detailviewitemProfileImage,data!!.postAdminProfile)
                   detailviewitemProfileTextview.setText(data!!.postAdmin)
                   loadImage(detailviewitemImageviewContent,"postImage",data!!.imageNames[0],data!!.postAdmin)
                   detailviewitemFavoritecounterTextview.setText("좋아요 "+data!!.like.toString()+"개")
                   detailviewitemExplainTextview.setText(data!!.mainText)
                   detailviewitemExplainTextview3.setText(data!!.uploadDate.substring(0,13))
                   detailviewitemCommentCOuntTextview.setText("댓글 "+data!!.commentCount.toString()+ "개 모두 보기")

                   //Post속성 중 좋아요 누른 멤버리스트에서 현재유저가 있는지 확인
                   for (likeMemeber in data!!.likes) {
                       if (likeMemeber == currentUserEamil) {
                           liking = true
                           break
                       } else {
                           liking = false
                       }
                   }

                   //현재 좋아요 여부에 따라 좋아요 이미지 구성
                   if (liking == true) {
                       binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_24)
                   } else {
                       binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                   }

//댓글 모두보기 누르면
                   binding.detailviewitemCommentCOuntTextview.setOnClickListener {
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
                                                   data!!.uploadDate
                                               )//uploadDate가 같은, 즉 해당 position 포스트
                                               .get()//가져와서
                                               .addOnSuccessListener { documents ->
                                                   for (document in documents) { //for-in 이지만 포스트 하나임
                                                       Log.d("postId2", document.id)
                                                       usernameANDpostid = listOf(
                                                           userdocument.id,
                                                           document.id
                                                       ) //userEmail, postid
                                                       Log.d(
                                                           "postId3",
                                                           usernameANDpostid.toString()
                                                       )
                                                       val action =
                                                           OnePostFragmentDirections.actionOnePostFragmentToCommentFragment(
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
                   binding.detailviewitemCommentImageview.setOnClickListener {
                       //navController.navigate(R.id.action_peedFragment_to_commentFragment)
                       var usernameANDpostid = listOf<String>()
                       CoroutineScope(Dispatchers.IO).launch {
                           runBlocking {
                               db.collection("users").get() //모든 유저
                                   .addOnSuccessListener { userdocuments ->
                                       for (userdocument in userdocuments) {
                                           Log.d("document.id", userdocument.id)
                                           db.collection("users").document(userdocument.id).collection("Post") //모든 유저 포스트에서
                                               .whereEqualTo("uploadDate", data!!.uploadDate)//uploadDate가 같은, 즉 해당 position 포스트
                                               .get()//가져와서
                                               .addOnSuccessListener { documents ->
                                                   for (document in documents) { //for-in 이지만 포스트 하나임
                                                       Log.d("postId2", document.id)
                                                       usernameANDpostid = listOf(userdocument.id, document.id) //userEmail, postid
                                                       Log.d("postId3", usernameANDpostid.toString())
                                                       val action =
                                                           OnePostFragmentDirections.actionOnePostFragmentToCommentFragment(
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
                   binding.detailviewitemFavoriteImageview.setOnClickListener {
                       if (liking == false) {//좋아요가 안눌려 있으면

                           //like +1
                           incrementLike(data)

                           //좋아요 상태 true
                           liking = true

                           //이미지 변경
                           binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_24)

                           //피드 좋아요 텍스트 업데이트
                           binding.detailviewitemFavoritecounterTextview.text =
                               "좋아요 " + (data.like + 1).toString() + "개" //피드 좋아요 개수 없데이트

                           //알림 생성
                           //피드에서 좋아요 알림림 이 기능을 좋아요 카운트 하는곳에 넣어줌


                           //좋아요 최초 1회 눌렀을때만 알림 생성
                           if (firstLiking == true) {
                               //알림 생성
                               var alarmDTO = AlarmDTO()
                               alarmDTO.destinationUid =userName
                               alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
                               alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
                               alarmDTO.kind = 0
                               alarmDTO.message ="${MyApplication.prefs.getString("name","")}님이 좋아요를 눌렀습니다."
                               alarmDTO.timestamp = System.currentTimeMillis()
                               FirebaseFirestore.getInstance().collection("users").document(userName)
                                   .collection("Alarm").document().set(alarmDTO)
                           }
                       } else {// 좋아요가 눌려있으면
                           //좋아요 상태 fasle
                           liking = false

                           //빈 하트 변경
                           binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                           //like -1
                           decrementLike(data)

                           //피드 좋아요 텍스트 업데이트
                           binding.detailviewitemFavoritecounterTextview.text =
                               "좋아요 " + (data!!.like).toString() + "개"

                       }
                   }

               }
           }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    fun loadProfileImage(imageView: ImageView, fileName: String) {
        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
        val storageRef: StorageReference = storage.reference
        if (fileName != null) {
            storageRef.child("userProfileImage/${fileName}").downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("userProfileImage2", "userProfileImage3/${fileName}")
                    Glide.with(mainActivity)
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
    //image 불러오기
    fun loadImage(imageView: ImageView, folderName: String, fileName: String, userName: String) {
        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
        val storageRef: StorageReference = storage.reference
        storageRef.child("${folderName}/${userName}/${fileName}").downloadUrl.addOnCompleteListener { task ->
            Log.d("Imagepath = ", "${folderName}/${userName}/${fileName}")
            if (task.isSuccessful) {
                Glide.with(mainActivity)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    fun notifyPostMessage(name: String): String {
        return "${name}님이 회원님의 게시글을 좋아합니다."
    }


    fun incrementLike( data: Posts) {
        Log.d("cpItemList[position].uploadDat", data.uploadDate.toString())
        db.collection("users").get()
            .addOnSuccessListener { userdocuments ->
                for (userdocument in userdocuments) {
                    Log.d("document.id", userdocument.id)
                    db.collection("users").document(userdocument.id).collection("Post")
                        .whereEqualTo("uploadDate", data.uploadDate)
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

    fun decrementLike(data: Posts) {
        Log.d("cpItemList[position].uploadDat", data.uploadDate.toString())
        db.collection("users").get()
            .addOnSuccessListener { userdocuments ->
                for (userdocument in userdocuments) {
                    Log.d("document.id", userdocument.id)
                    db.collection("users").document(userdocument.id).collection("Post")
                        .whereEqualTo("uploadDate", data.uploadDate)
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
}