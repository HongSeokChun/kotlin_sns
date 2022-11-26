package com.android.example.hongseokchun.ui.peed

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentEditPostBinding
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.ui.friend.FriendPageFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UploadPostFragment : BaseFragment<FragmentEditPostBinding>(R.layout.fragment_edit_post) {
    private lateinit var imageAdapter: ImageAdapter
    private var imageUrlList: ArrayList<Uri> = ArrayList()
    val PERMISSION_Album = 101 // 앨범 권한 처리
    val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
    private val db = Firebase.firestore

    val storageRef: StorageReference = storage.reference
    private lateinit var layoutIndicator: LinearLayout

    override fun initStartView() {
        super.initStartView()
    }

    @SuppressLint("SimpleDateFormat", "SuspiciousIndentation")
    override fun initDataBinding() {
        super.initDataBinding()
        (activity as MainActivity).setNavShow("view")

        imageAdapter = ImageAdapter(imageUrlList)
        binding.viewPager2.adapter = imageAdapter
        layoutIndicator = binding.layoutIndicators


        // 갤러리에서 이미지 선택 클릭시
        binding.uploadImage.setOnClickListener {
            //갤러리 접근 권한확인
            val readPermission = context?.let { it1 ->
                ActivityCompat.checkSelfPermission(
                    it1,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

            if (readPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_Album
                )
            } else {
                openGallery()  //갤러리 열기
            }
        }

        // viewPager 변경
        binding.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        //공유 클릭시
        binding.btnShare.setOnClickListener {
            binding.btnShare.isEnabled=false
            val message :String = binding.postMessage.text.toString()
            val now = Date()
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초")
            val date= dateFormat.format(now)


                if (imageUrlList.size > 0) {
                    uploadPhoto(date,message,
                        mSuccessHandler = {
//                            Toast.makeText(context, "게시글 업로드 성공", Toast.LENGTH_SHORT).show()
                        },
                        mErrorHandler = {
//                            Toast.makeText(context, "게시글 업로드에 실패했습니다", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    // 이미지 uri가 존재하지 않는 경우
                    Toast.makeText(context, "사진을 선택해주세요.",Toast.LENGTH_SHORT).show()
                    binding.btnShare.isEnabled=true
                }
            binding.btnShare.isEnabled=true
            binding.postMessage.text.clear()

            navController.navigate(R.id.action_editPostFragment_self)

        }

    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    //갤러리 열기
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.action = Intent.ACTION_PICK
        requestActivity.launch(intent)
    }

    private val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data?.data != null) { //갤러리 캡쳐 결과값
                val clipData = it?.data?.clipData
                val clipDataSize = clipData?.itemCount
                if (clipData == null) { //이미지를 하나만 선택할 경우 clipData가 null이 올수 있음
                    val selectedImageUri = it?.data?.data!!
                    imageUrlList.add(selectedImageUri)
                    imageAdapter = ImageAdapter(imageUrlList)
                    binding.viewPager2.adapter = imageAdapter
                    binding.uploadImage.visibility = View.GONE
                    binding.viewPager2.visibility = View.VISIBLE
                    setupIndicators(imageUrlList.size)


                } else {
                    clipData.let { clipData ->
                        for (i in 0 until clipDataSize!!) { //선택 한 사진수만큼 반복
                            val selectedImageUri = clipData.getItemAt(i).uri
                            imageUrlList.add(selectedImageUri)
                            Log.d(" imageUrlList size ", imageUrlList.size.toString())
                        }
                    }
                    imageAdapter = ImageAdapter(imageUrlList)
                    binding.viewPager2.adapter = imageAdapter
                    binding.uploadImage.visibility = View.GONE
                    binding.viewPager2.visibility = View.VISIBLE
                    setupIndicators(imageUrlList.size)
                }

            }
        }

    // 현재 사진 목록 ... 만들기
    private fun setupIndicators(count: Int) {
        val indicators: Array<ImageView?> = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 8, 16, 8)
        for (i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i]?.setImageDrawable(
                context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.bg_indicator_inactive
                    )
                }
            )
            indicators[i]?.layoutParams = params
            layoutIndicator.addView(indicators[i])
        }
        setCurrentIndicator(0)
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount: Int = layoutIndicator.childCount
        for (i in 0 until childCount) {
            val imageView: ImageView = layoutIndicator.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.bg_indicator_active
                        )
                    }
                )
            } else {
                imageView.setImageDrawable(
                    context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.bg_indicator_inactive
                        )
                    }
                )
            }
        }
    }

    // 게시물 업로드
    private fun uploadPhoto(
        date: String,
        message: String,
        mSuccessHandler: (String) -> Unit,
        mErrorHandler: () ->Unit,
    ){
        val fileNames: ArrayList<String> =ArrayList()
        for((i,uri) in imageUrlList.withIndex()) {
            val fileName = "${date}_${i}"
            fileNames.add(fileName)
            storage.reference.child("postImage/${prefs.getString("email","null")}").child(fileName)
                .putFile(uri)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // 파일 업로드에 성공했기 때문에 파일을 다시 받아 오도록 해야함
                        storage.reference.child("postImage/${prefs.getString("email","null")}")
                            .child(fileName).downloadUrl
                            .addOnSuccessListener { uri ->
                                mSuccessHandler(uri.toString())
                            }.addOnFailureListener {
                                mErrorHandler()
                            }
                    } else {
                        mErrorHandler()
                    }
                }
        }
//        val newPost = Posts("",fileNames,HashMap(),date,0,message)
        val newPost = Posts(prefs.getString("email","null")
            ,fileNames,date,0,message,0,prefs.getString("profileImg","null"),
            ArrayList()
        )
        // 파이어베이스에 게시물정보 저장
                db.collection("users").document(prefs.getString("email","null"))
                    .collection("Post").add(newPost)
    }
}