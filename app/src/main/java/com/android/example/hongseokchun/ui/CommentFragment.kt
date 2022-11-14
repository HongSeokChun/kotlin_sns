package com.android.example.hongseokchun.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentCommentBinding

class CommentFragment : BaseFragment<FragmentCommentBinding>(R.layout.fragment_comment) {
    private lateinit var commentAdapter: CommentAdapter

    override fun initStartView() {
        super.initStartView()
    }

    override fun initDataBinding() {
        super.initDataBinding()


        binding.commentRecyclerview.setHasFixedSize(true)
        binding.commentRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.commentRecyclerview.adapter = commentAdapter


        binding.commentBtnSend.setOnClickListener {
            //comments.add(0, Comment(Student(2,"hoy"), "new comments"))
            commentAdapter.notifyItemInserted(0)
          }

    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}
