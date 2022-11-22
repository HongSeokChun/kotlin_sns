package com.android.example.hongseokchun.ui.mypage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentEditProfileBinding
import com.android.example.hongseokchun.databinding.FragmentFriendListBinding
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_friend_list) {
    val db = Firebase.firestore


    private val UserviewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }


    override fun initStartView() {
        super.initStartView()
    }

    override fun initDataBinding() {
        super.initDataBinding()
//        binding.editTxtmodname.text = "user_name"
//        loadImage(binding.profile,"profile_img1","")
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    //image 불러오기
    fun loadImage(imageView: ImageView, fileName: String, userName: String) {
        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
        val storageRef: StorageReference = storage.reference
        storageRef.child("postImage/${userName}/${fileName}.jpg").downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
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