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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private lateinit var myPageAdapter: MyPageAdapter
//    private lateinit var swipe: SwipeRefreshLayout
    val db = Firebase.firestore
    private var cuurentUserEmail: String? = null
    lateinit var mainActivity: MainActivity


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

        swipe = binding.swipe
        cuurentUserEmail = Firebase.auth.currentUser?.email
        cuurentUserEmail?.let { Log.d("currentUserEamil", it) }
        myPageAdapter = MyPageAdapter(mutableListOf())



        val currentUserName = ArrayList<String>()
        cuurentUserEmail?.let {
            currentUserName.add(it) //Peed Livedata 때매 list
            userViewModel.getUser(it)
        }
        userViewModel.userLiveData.observe(viewLifecycleOwner){
            loadProfileImage(binding.accountIvProfile,it.profileUrl)
            Log.d("it.profileUrl",it.profileUrl)
        }


        var ref = db.collection("users").document(cuurentUserEmail!!)//currentUserName.get(0))
        ref.get()
            .addOnSuccessListener { document->
                val data = document.toObject<User>()
                if (data != null) {
                    binding.accountTvFollowingCount.setText(data.following.size.toString())
                    binding.accountTvFollowerCount.setText(data.follower.size.toString())
                    binding.profileName.setText(data.name)
                }
            }



        ref.collection("Post")
            .get()
            .addOnSuccessListener { documents->
                binding.accountTvPostCount.setText(documents.size().toString())
            }

        peedViewModel.getPosts(currentUserName)//현재 user email

        peedViewModel.peedLiveData.observe(viewLifecycleOwner) { itemList ->
            myPageAdapter.itemList = itemList

            Log.d("peeditemList", itemList.toString())
        }

        Log.d("point1","")
        binding.accountRecyclerview.setHasFixedSize(true)
        Log.d("point2","")
        binding.accountRecyclerview.layoutManager = GridLayoutManager(context, 3)
        Log.d("point3","")
        binding.accountRecyclerview.adapter = myPageAdapter

        val mActivity = activity as MainActivity



        Log.d("point4","")

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

            swipe.isRefreshing = false
            Log.d("point5","")
        }



    }
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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
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