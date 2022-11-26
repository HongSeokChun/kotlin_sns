package com.android.example.hongseokchun.ui.friend

import android.annotation.SuppressLint
import android.content.ContentValues
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentFollowerListBinding
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.FollowUser
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FollowerUserFragment  : BaseFragment<FragmentFollowerListBinding>(R.layout.fragment_follower_list) {
    private lateinit var friendAdapter: FriendAdapter
    val db = Firebase.firestore
    val loginUser = prefs.getString("email","null")
    var auth: FirebaseAuth = Firebase.auth
    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("view")
    }


    override fun initDataBinding() {
        super.initDataBinding()
        friendAdapter= FriendAdapter(ArrayList())

        viewModel.getflollowerUsers()

        binding.recyclerView.adapter=friendAdapter

        viewModel.followerUserLiveData.observe(viewLifecycleOwner) { itemList ->
            friendAdapter.itemList = itemList
            val n = itemList.size;
            binding.friendNum.text = "${n}명"
            Log.d("recipee",itemList.toString())
        }


        //친구검색
        binding.etSearchFriend.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchString = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("not implemented")
                // To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchItemList: ArrayList<HashMap<String,String>> = ArrayList<HashMap<String,String>>()
                val searchString = binding.etSearchFriend.text
                viewModel.followerUserLiveData.observe(viewLifecycleOwner) { itemList ->
                    for (item in itemList){
                        if(item.get("name")?.contains(searchString) == true){
                            searchItemList.add(item)
                        }
                    }
                    friendAdapter.itemList = searchItemList

                    if (binding.etSearchFriend.text.isEmpty()) {
                        viewModel.followerUserLiveData.observe(viewLifecycleOwner) { itemList ->
                            friendAdapter.itemList = itemList
                        }
                    }
                }
            }
        })


        //팔로우,팔로잉 버튼 클릭
        friendAdapter.setItemClickListener(object: FriendAdapter.OnItemClickListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onClick(btn: String, position: Int) {
                // 클릭 시 이벤트 작성
                val friendName = friendAdapter.itemList[position].get("name")
                val dataOrigin = friendAdapter.itemList[position]

                if(btn =="팔로우") {
                    // 원래 친구목록에 있으면 삭제
                    db.collection("users").document(loginUser)
                        .update("following", FieldValue.arrayRemove(dataOrigin))
                    // 친구 DB에 반영
                    changeFollowState(false)
                }
                else if(btn=="팔로잉") {
                    //없으면 추가
                    db.collection("users").document(loginUser)
                        .update("following", FieldValue.arrayUnion(dataOrigin))
                    // 친구 DB에 반영
                    changeFollowState(true)


                }
                else{
                    // 친구 프로필 보기
                    parentFragmentManager.beginTransaction().apply{
                        replace(R.id.container, FriendPageFragment())
                        addToBackStack(null)
                        commit()
                    }
                }
            }

        })
    }

    fun changeFollowState(following:Boolean){
       val myProfile = FollowUser(loginUser, prefs.getString("name","null"), prefs.getString("profileImg","null"))

        if(following) {
            // 친구 팔로워 목록에 내 정보 업로드 -> 팔로잉
            db.collection("users").document(prefs.getString("watchUser", "null"))
                .update("follower", FieldValue.arrayUnion(myProfile))

        }else {
            // 친구 팔로워 목록에 내 정보 삭제 -> 팔로잉 취소
            db.collection("users").document(prefs.getString("watchUser", "null"))
                .update("follower", FieldValue.arrayRemove(myProfile))
        }
    }
    override fun initAfterBinding() {
        super.initAfterBinding()
    }
    // 팔로워 추가 했을 때 알람 기능
    fun followerAlarm(destinationUid : String){
        var alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = auth?.currentUser?.email
        alarmDTO.uid = auth?.currentUser?.uid
        alarmDTO.timestamp = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)

    }
}