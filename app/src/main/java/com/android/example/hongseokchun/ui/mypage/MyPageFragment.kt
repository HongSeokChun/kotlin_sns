package com.android.example.hongseokchun.ui.mypage

import androidx.recyclerview.widget.GridLayoutManager
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentMyPageBinding

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val posts = mutableListOf(Post(1, "2022-11-14", "22", "hi im jhon"), Post(1, "2022-11-16", "2", "no"))

    private lateinit var myPageAdapter: MyPageAdapter

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("view")
    }

    override fun initDataBinding() {
        super.initDataBinding()

        myPageAdapter = MyPageAdapter(posts)
        binding.accountRecyclerview.setHasFixedSize(true)
        binding.accountRecyclerview.layoutManager = GridLayoutManager(context, 3)
        binding.accountRecyclerview.adapter = myPageAdapter
        binding.accountIvProfile.setImageResource(R.drawable.sample)


        binding.buttonAdd.setOnClickListener {
            posts.add(0, Post(1, "2022-11-14", "22", "hi im jhon"))
//            postID++
            myPageAdapter.notifyItemInserted(0)
        }

    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}