package com.android.example.hongseokchun.ui.friend

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentFriendPageBinding
import com.android.example.hongseokchun.ui.mypage.MyPageAdapter

class FriendPageFragment : BaseFragment<FragmentFriendPageBinding>(R.layout.fragment_friend_page) {
    private lateinit var myPageAdapter: MyPageAdapter

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("view")
    }

    override fun initDataBinding() {
        super.initDataBinding()
        myPageAdapter = MyPageAdapter(mutableListOf())

        binding.accountRecyclerview.setHasFixedSize(true)
        binding.accountRecyclerview.layoutManager = GridLayoutManager(context, 3)
        binding.accountRecyclerview.adapter = myPageAdapter

        binding.accountIvProfile.setImageResource(R.drawable.sample) //user profile
        val mActivity = activity as MainActivity


    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}
