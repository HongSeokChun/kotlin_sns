package com.android.example.hongseokchun.ui.mypage

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.recyclerview.widget.GridLayoutManager
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.MySharedPreferences
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentMyPageBinding
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.PeedAdapter
import com.android.example.hongseokchun.viewmodel.PeedViewModel
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private lateinit var myPageAdapter: MyPageAdapter
//    private lateinit var swipe: SwipeRefreshLayout
    val db = Firebase.firestore


    private val userViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }


    private val peedViewModel by lazy {
        ViewModelProvider(this)[PeedViewModel::class.java]
    }

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("view")
    }

    override fun initDataBinding() {
        super.initDataBinding()
//        swipe = binding.swipe

        myPageAdapter = MyPageAdapter(mutableListOf())

        val currentUserName = ArrayList<String>()
        currentUserName.add("hong@hong.hong")
        peedViewModel.getPosts(currentUserName)//현재 user email

        peedViewModel.peedLiveData.observe(viewLifecycleOwner) { itemList ->
            myPageAdapter.itemList = itemList
            Log.d("peeditemList", itemList.toString())
        }

        binding.accountRecyclerview.setHasFixedSize(true)
        binding.accountRecyclerview.layoutManager = GridLayoutManager(context, 3)
        binding.accountRecyclerview.adapter = myPageAdapter

        val mActivity = activity as MainActivity

        getUserInfo()

        binding.btnSetting.setOnClickListener {
            SettingDialog().show(parentFragmentManager, "preference")
        }




    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        swipe.setOnRefreshListener {
//            val ft = parentFragmentManager.beginTransaction()
//            ft.detach(this).attach(this).commit()
//            initDataBinding()
//
//            swipe.isRefreshing = false
//        }




    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    // 유저 정보 가져오기
    fun getUserInfo(){
        db.collection("users").document(prefs.getString("email","null")).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                if (data != null) {
                    binding.accountTvFollowingCount.text = data.following.size.toString()
                    binding.accountTvFollowerCount.text = data.follower.size.toString()
                    binding.userName.text=data.name
                    loadImage(binding.accountIvProfile,data.profile_img)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
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
}