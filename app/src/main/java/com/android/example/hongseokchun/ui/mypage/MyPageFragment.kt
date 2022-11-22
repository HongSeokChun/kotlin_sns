package com.android.example.hongseokchun.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.android.example.hongseokchun.MainActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentMyPageBinding
import com.android.example.hongseokchun.ui.PeedAdapter
import com.android.example.hongseokchun.viewmodel.PeedViewModel
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private lateinit var myPageAdapter: MyPageAdapter
    private lateinit var swipe: SwipeRefreshLayout
    val db = Firebase.firestore



    private val peedViewModel by lazy {
        ViewModelProvider(this)[PeedViewModel::class.java]
    }

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("view")
    }

    override fun initDataBinding() {
        super.initDataBinding()
        swipe = binding.swipe

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

        binding.accountIvProfile.setImageResource(R.drawable.sample) //user profile
        val mActivity = activity as MainActivity


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener {
            val ft = parentFragmentManager.beginTransaction()
            ft.detach(this).attach(this).commit()
            initDataBinding()

            swipe.isRefreshing = false
        }

        binding.accountBtnProfileEdit.setOnClickListener {

        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}