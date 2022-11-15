package com.android.example.hongseokchun.ui.mypage

import androidx.recyclerview.widget.GridLayoutManager
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentMyPageBinding
import com.android.example.hongseokchun.ui.peed.MyPageAdapter

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_mypage) {
    private lateinit var myPageAdapter: MyPageAdapter

    override fun initStartView() {
        super.initStartView()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        binding.accountRecyclerview.setHasFixedSize(true)
        binding.accountRecyclerview.layoutManager = GridLayoutManager(context, 3)
        binding.accountRecyclerview.adapter = myPageAdapter
        binding.accountIvProfile.setImageResource(R.drawable.sample)


        binding.buttonAdd.setOnClickListener {
//            posts.add(0, Post(1, "2022-11-14", "22", "hi im jhon"))
//            postID++
            myPageAdapter.notifyItemInserted(0)
        }

    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}