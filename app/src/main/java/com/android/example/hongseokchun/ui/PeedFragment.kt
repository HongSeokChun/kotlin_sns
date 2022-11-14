package com.android.example.hongseokchun.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentPeedBinding

class PeedFragment: BaseFragment<FragmentPeedBinding>(R.layout.fragment_peed) {
    private lateinit var PeedAdapter: MyAdapter

    override fun initStartView() {
        super.initStartView()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = PeedAdapter

        binding.buttonAdd.setOnClickListener {
//            students.add(0, Student(stdID, "test $stdID"))
//            posts.add(0,Post(1, "2022-11-14","22","hi im jhon"))
//            stdID++
//            postID++
            PeedAdapter.notifyItemInserted(0)
        }
    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}