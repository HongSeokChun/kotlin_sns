package com.android.example.hongseokchun.ui

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentEditPostBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class UploadPostFragment : BaseFragment<FragmentEditPostBinding>(R.layout.fragment_edit_post) {
    private lateinit var imageAdapter: ImageAdapter
    private var imageUrlList: ArrayList<Uri> = ArrayList()
    val PERMISSION_Album = 101 // 앨범 권한 처리
    private val db = Firebase.firestore
    private lateinit var layoutIndicator: LinearLayout

    override fun initStartView() {
        super.initStartView()
    }

    @SuppressLint("SimpleDateFormat")
    override fun initDataBinding() {
        super.initDataBinding()


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
        binding.share.setOnClickListener {
            val message :String = binding.postMessage.text.toString()
            val now = Date()
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초")
            val date= dateFormat.format(now)
            val Postdata = hashMapOf(
                "message" to message,
                "images" to imageUrlList,
                "uploadDate" to date
            )

            // 파이어베이스에 게시물정보 저장
            db.collection("users").document("hongseokchun@naver.com")
                .update("post", FieldValue.arrayUnion(Postdata))

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

}