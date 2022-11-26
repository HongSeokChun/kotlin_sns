package com.android.example.hongseokchun.ui.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.example.hongseokchun.MyApplication
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentEditProfileBinding
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.peed.ImageAdapter
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.ArrayList
import java.util.HashMap

class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {
    val db = Firebase.firestore
    val PERMISSION_Album = 101 // 앨범 권한 처리
    val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
    lateinit var uri:Uri
    lateinit var editName:String
    lateinit var editBirth:String


    private val UserviewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }


    override fun initStartView() {
        super.initStartView()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        binding.editTxtmodname.hint = prefs.getString("name","")
        binding.editTextmodage.hint = prefs.getString("birth","")
        loadImage(binding.profile)
    }

    override fun initAfterBinding() {
        super.initAfterBinding()


        binding.profile.setOnClickListener {
            openGallery()
        }
        binding.textView5.setOnClickListener {
            openGallery()
        }
        binding.modBtn.setOnClickListener {
            if(binding.editTextmodage.text.toString() == "")
                editBirth = prefs.getString("birth","")
            else
                editBirth = binding.editTextmodage.text.toString()
            if(binding.editTxtmodname.text.toString() == "")
                editName = prefs.getString("name","")
            else
                editName = binding.editTxtmodname.text.toString()

            editUserFireabse(editName,editBirth)
            navController.navigate(R.id.action_editProfileFragment_to_myPageFragment)


        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }


    //갤러리 열기
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.action = Intent.ACTION_PICK
        requestActivity.launch(intent)
    }

    private val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data?.data != null) { //갤러리 캡쳐 결과값
                val selectedImageUri = it?.data?.data!!
                binding.profile.setImageURI(selectedImageUri)
                uploadPhoto(selectedImageUri, mSuccessHandler = {
//                            Toast.makeText(context, "게시글 업로드 성공", Toast.LENGTH_SHORT).show()
                },
                    mErrorHandler = {
//                            Toast.makeText(context, "게시글 업로드에 실패했습니다", Toast.LENGTH_SHORT).show()
                    })

            }
        }

    private fun uploadPhoto(
        uri : Uri,
        mSuccessHandler: (String) -> Unit,
        mErrorHandler: () ->Unit,
    ){
        val fileName = prefs.getString("email","")
        storage.reference.child("userProfileImage").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // 파일 업로드에 성공했기 때문에 파일을 다시 받아 오도록 해야함
                    storage.reference.child("userProfileImage")
                        .child(fileName).downloadUrl
                        .addOnSuccessListener { uri ->
                            uploadImageToFirebase(uri)
                            mSuccessHandler(uri.toString())
                        }.addOnFailureListener {
                            mErrorHandler()
                        }
                } else {
                    mErrorHandler()
                }
            }
        }

    fun uploadImageToFirebase(uri: Uri){
        db.collection("users").document(prefs.getString("email","null"))
            .update("profile_img", uri)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun editUserFireabse(name:String, date:String){
        db.collection("users").document(prefs.getString("email","null"))
            .update("name",name)
        db.collection("users").document(prefs.getString("email","null"))
            .update("birth",date)
    }


    //image 불러오기
    fun loadImage(imageView: ImageView){
        context?.let {
            Glide.with(it)
                .load(prefs.getString("profileImg",""))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
        }
    }
}



