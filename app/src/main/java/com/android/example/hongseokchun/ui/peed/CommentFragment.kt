//package com.android.example.hongseokchun.ui.peed
//
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.android.example.hongseokchun.MainActivity
//import com.android.example.hongseokchun.R
//import com.android.example.hongseokchun.base.BaseFragment
//import com.android.example.hongseokchun.databinding.FragmentCommentBinding
//import com.android.example.hongseokchun.ui.mypage.Student
//
//class CommentFragment : BaseFragment<FragmentCommentBinding>(R.layout.fragment_comment) {
//    private lateinit var commentAdapter: CommentAdapter
//    private val comments =  mutableListOf(Comment(Student(1,"john"),"2022-11-16","12" ,"hello this is comment"))
//
//    override fun initStartView() {
//        super.initStartView()
//        (activity as MainActivity).setNavShow("view")
//    }
//
//    override fun initDataBinding() {
//        super.initDataBinding()
//
//        commentAdapter = CommentAdapter(comments)
//        binding.commentRecyclerview.setHasFixedSize(true)
//        binding.commentRecyclerview.layoutManager = LinearLayoutManager(context)
//        binding.commentRecyclerview.adapter = commentAdapter
//
//
//        binding.commentBtnSend.setOnClickListener {
//            //comments.add(0, Comment(Student(2,"hoy"), "new comments"))
//            commentAdapter.notifyItemInserted(0)
//          }
//
//    }
//
//
//    override fun initAfterBinding() {
//        super.initAfterBinding()
//    }
//
//}
