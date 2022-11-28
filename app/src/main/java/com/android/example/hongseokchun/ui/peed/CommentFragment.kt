
package com.android.example.hongseokchun.ui.peed

import android.content.ContentValues
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
import com.android.example.hongseokchun.MyApplication
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentCommentBinding
import com.android.example.hongseokchun.databinding.PeedPostItemBinding
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.Comment
import com.android.example.hongseokchun.model.Notify
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.ui.MyViewHolder
import com.android.example.hongseokchun.ui.PeedAdapter
import com.android.example.hongseokchun.viewmodel.PeedViewModel
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
    private var uploadDate:String? = null

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

                db.collection("Posts").document("cart@naver.com").get()
                    .addOnSuccessListener { documentSnapshot ->
                        val data = documentSnapshot.toObject<Posts>()
                        if (data != null) {
                            uploadDate=data.uploadDate
                        }
                        Log.d("posts repo",data.toString())
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
                    }


                //해당 게시물에 Comment 컬렉션 생성
                db.collection("users").document(userName).collection("Post")
                    .document(postid).collection("Comments")
                    .document().set(newComment)
                binding.commentEditMessage.setText("") //댓글 입력창 초기화

                //피드에서 좋아요 알림림 이 기능을 좋아요 카운트 하는곳에 넣어줌
                var alarmDTO = AlarmDTO()
                alarmDTO.destinationUid =userName
                alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
                alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
                alarmDTO.kind = 0
                alarmDTO.message ="${MyApplication.prefs.getString("name","")}님이 댓글을 달았습니다."
                alarmDTO.timestamp = System.currentTimeMillis()
                alarmDTO.uploadDate = uploadDate

                FirebaseFirestore.getInstance().collection("users").document(userName)
                    .collection("Alarm").document().set(alarmDTO)
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
