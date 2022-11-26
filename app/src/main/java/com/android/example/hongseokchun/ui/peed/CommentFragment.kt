
package com.android.example.hongseokchun.ui.peed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentCommentBinding
import com.android.example.hongseokchun.databinding.PeedPostItemBinding
import com.android.example.hongseokchun.model.Comment
import com.android.example.hongseokchun.model.Notify
import com.android.example.hongseokchun.ui.MyViewHolder
import com.android.example.hongseokchun.ui.PeedAdapter
import com.android.example.hongseokchun.viewmodel.PeedViewModel
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CommentFragment : BaseFragment<FragmentCommentBinding>(R.layout.fragment_comment) {
    private lateinit var commentAdapter: CommentAdapter
    val args: CommentFragmentArgs by navArgs()
    private val db = Firebase.firestore
    private var comments = mutableListOf<Comment>()
     private lateinit var swipe: SwipeRefreshLayout
    private var currentUserEmail:String? = null

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
        swipe = binding.swipe // 당겨 새로고침
        currentUserEmail = Firebase.auth.currentUser?.email

        Log.d("deliverayArgs",args.nameANDpostId[0])
         //PostFragment로부터 전달받은 postAdmin과 post id
        val userName = args.nameANDpostId[0]
        val postid = args.nameANDpostId[1]

        db.collection("users").document(userName).collection("Post")
            .document(postid).collection("Comments") //해당 포스트 댓글 가져오기
            .get()
            .addOnSuccessListener { documents ->
                val data = arrayListOf<Comment>()
                Log.d("Comment.documents.size()",documents.size().toString())
                for (document in documents) {
                    Log.d("comment_id",document.id)
                    Log.d("document.toObject<Comment>()",document.toObject<Comment>().comment.toString())
                    if(document.toObject<Comment>().comment != "") //"" Comment 제외
                        data.add(document.toObject<Comment>())
                }
                commentAdapter = CommentAdapter(data) // 가져온 COmment로 adpater 구성
                binding.commentRecyclerview.setHasFixedSize(true)
                binding.commentRecyclerview.layoutManager = LinearLayoutManager(context)
                binding.commentRecyclerview.adapter = commentAdapter
            }

        //SEnd 눌렀을 때
        binding.commentBtnSend.setOnClickListener {
            //new Comment 구성1
            val now = Date()
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초")
            val date= dateFormat.format(now)

            userViewModel.getUser(currentUserEmail!!)
            userViewModel.userLiveData.observe(viewLifecycleOwner) { //current user 정보 가져오기

                //new Comment 구성2
                val currentUserName = it.name
                val currentUserProfileUrl = it.profile_img

                //new COmment 생성
                var newComment = Comment(
                    currentUserName,
                    binding.commentEditMessage.text.toString(),
                    currentUserProfileUrl,
                    date
                )

                //해당 게시물에 Comment 컬렉션 생성
                db.collection("users").document(userName).collection("Post")
                    .document(postid).collection("Comments")
                    .document().set(newComment)
                binding.commentEditMessage.setText("") //댓글 입력창 초기화

                //알림 구성
                var notificationPost =
                    Notify(
                        currentUserProfileUrl,
                        currentUserName,
                        notifyCommentMessage(currentUserName)
                    )
                //알림 생성
                db.collection("users").document(userName).collection("Notification")
                    .document()
                    .set(notificationPost)
            }

            //해당 post에 댓글 수 증가
            db.collection("users").document(userName).collection("Post").document(postid)
                .update("commentCount",FieldValue.increment(1))

            //새로고침
            initDataBinding()

        }

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener {
            val ft = parentFragmentManager.beginTransaction()
            ft.detach(this).attach(this).commit()
            initDataBinding()

            swipe.isRefreshing = false
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    fun notifyCommentMessage(name: String): String {
        return "${name}님이 회원님의 게시글에 댓글을 작성했습니다."
    }
}
