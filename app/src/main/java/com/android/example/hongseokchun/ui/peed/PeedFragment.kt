package com.android.example.hongseokchun.ui.peed

import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentPeedBinding
import com.android.example.hongseokchun.ui.PeedAdapter
import com.android.example.hongseokchun.ui.mypage.Post
import com.android.example.hongseokchun.ui.mypage.Student

class PeedFragment: BaseFragment<FragmentPeedBinding>(R.layout.fragment_peed) {
    private lateinit var peedAdapter: PeedAdapter
    private val students = mutableListOf(Student(1, "john"), Student(2, "tom"))
    private val posts = mutableListOf(Post(1, "2022-11-14", "22", "hi im jhon"), Post(1, "2022-11-16", "2", "no"))
    override fun initStartView() {
        super.initStartView()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        peedAdapter = PeedAdapter(students, posts)

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = peedAdapter

        binding.buttonAdd.setOnClickListener {
            students.add(0, Student(0, "test $00"))
            posts.add(0,Post(1, "2022-11-14","22","hi im jhon"))

            peedAdapter.notifyItemInserted(0)
        }
    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}