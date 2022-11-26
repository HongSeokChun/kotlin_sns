package com.android.example.hongseokchun.ui.friend

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentFriendPageBinding
import com.android.example.hongseokchun.model.FollowUser
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.mypage.MyPageAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FriendPageFragment() : BaseFragment<FragmentFriendPageBinding>(R.layout.fragment_friend_page) {
    private lateinit var fPageAdapter: MyPageAdapter
    val db = Firebase.firestore
    lateinit var userData : HashMap<String,String>
    val loginUser = prefs.getString("email","null")

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("")

//        getUserInfo()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        getUserInfo()

        fPageAdapter = MyPageAdapter(mutableListOf())

        binding.accountRecyclerview.setHasFixedSize(true)
        binding.accountRecyclerview.layoutManager = GridLayoutManager(context, 3)
        binding.accountRecyclerview.adapter = fPageAdapter

        binding.accountIvProfile.setImageResource(R.drawable.sample) //user profile



        //팔로잉 버튼 클릭시
        binding.accountBtnFollowSignout.setOnClickListener {
            if(binding.accountBtnFollowSignout.text == "팔로잉"){
                binding.accountBtnFollowSignout.text = "팔로우"
                binding.accountBtnFollowSignout.setBackgroundResource(R.drawable.follow_button)
                //db following 목록에서 삭제
                db.collection("users").document(prefs.getString("email","null"))
                    .update("following", FieldValue.arrayRemove(userData))
                changeFollowState(false)
            }
            else{
                binding.accountBtnFollowSignout.text = "팔로잉"
                binding.accountBtnFollowSignout.setBackgroundResource(R.drawable.following_button)
                //db following 목록에 추가
                db.collection("users").document(prefs.getString("email","null"))
                    .update("following", FieldValue.arrayUnion(userData))
                changeFollowState(true)
            }
        }

    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    // 유저 정보가져와서 데이터 입력
    fun getUserInfo() {
        val db = Firebase.firestore

        val userEmail = prefs.getString("watchUser","null")
        db.collection("users").document(userEmail).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject<User>()
                if (user != null) {
                    binding.userName.text= user.name
                    binding.accountTvFollowingCount.text=user.following.size.toString()
                    binding.accountTvFollowerCount.text = user.follower.size.toString()

                    userData = HashMap()
                    userData.put("name",user.name)
                    userData.put("profile_img",user.profile_img)
                    userData.put("email",user.email)

                    isFollow(user.name)
                    loadImage(binding.accountIvProfile,user.profile_img)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

    }

    // 로그인한 유저가 팔로우했는지 확인
    fun isFollow(name:String) {
        val db = Firebase.firestore

        db.collection("users").document(prefs.getString("email","null")).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                if (data != null) {
                    val followingList = data.following.toString()
                    if ( name in followingList) {
                        binding.accountBtnFollowSignout.text = "팔로잉"
                        binding.accountBtnFollowSignout.setBackgroundResource(R.drawable.following_button)
                    }
                    else{
                        binding.accountBtnFollowSignout.text = "팔로우"
                        binding.accountBtnFollowSignout.setBackgroundResource(R.drawable.follow_button)
                    }


                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }

    // firebase storage에서 이미지 불러오기

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

    fun changeFollowState(following:Boolean){
        val myProfile = FollowUser(loginUser, prefs.getString("name","null"), prefs.getString("profileImg","null"))

        if(following) {
            // 친구 팔로워 목록에 내 정보 업로드 -> 팔로잉
            db.collection("users").document(prefs.getString("watchUser", "null"))
                .update("follower", FieldValue.arrayUnion(myProfile))

        }else {
            // 친구 팔로워 목록에 내 정보 삭제 -> 팔로잉 취소
            db.collection("users").document(prefs.getString("watchUser", "null"))
                .update("follower", FieldValue.arrayRemove(myProfile))
        }
    }

}
