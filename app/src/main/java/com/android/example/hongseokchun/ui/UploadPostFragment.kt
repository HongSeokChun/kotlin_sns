package com.android.example.hongseokchun.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentEditPostBinding
import java.net.URL

class UploadPostFragment : BaseFragment<FragmentEditPostBinding>(R.layout.fragment_edit_post) {
    private lateinit var imageAdapter: ImageAdapter
    private var imageUrlList: ArrayList<Uri> = ArrayList()
    val PERMISSION_Album = 101 // 앨범 권한 처리

    override fun initStartView() {
        super.initStartView()
    }


    override fun initDataBinding() {
        super.initDataBinding()

        binding.uploadImage.setOnClickListener {
            //권한확인
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
                openGallery()
            }

            imageAdapter = ImageAdapter(imageUrlList)

            binding.viewPager2.adapter = imageAdapter
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
                }

            }
        }
}