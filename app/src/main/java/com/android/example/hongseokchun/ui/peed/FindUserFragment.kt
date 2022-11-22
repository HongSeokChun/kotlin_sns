package com.android.example.hongseokchun.ui.peed

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentFindUserBinding
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.friend.FriendAdapter
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FindUserFragment : BaseFragment<FragmentFindUserBinding>(R.layout.fragment_find_user) {
    private lateinit var friendAdapter: FriendAdapter
    val db = Firebase.firestore
    val UserList : ArrayList<HashMap<String,String>> = ArrayList()

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
                val searchString = binding.etSearchFriend.text.toString()

                getUserList()

                if (binding.etSearchFriend.text.isEmpty()) {
                    searchItemList.clear()
                    friendAdapter = FriendAdapter(searchItemList)
                    binding.recyclerView.adapter = friendAdapter

                }else{
                    for(data in UserList){
                        val name = data.get("name");
                        if (name != null) {
                            if(searchString in name){
                                searchItemList.add(data)
                            }
                        }
                    }
                    friendAdapter = FriendAdapter(searchItemList)
                    binding.recyclerView.adapter = friendAdapter
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
                    db.collection("users").document("cart@naver.com")
                        .update("following", FieldValue.arrayRemove(dataOrigin))
                }
                else {
                    //없으면 추가
                    db.collection("users").document("cart@naver.com")
                        .update("following", FieldValue.arrayUnion(dataOrigin))

                }
            }

        })


    }


    fun getUserList(){
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                UserList.clear()
                for (document in result) {
                    val user : HashMap<String,String> = HashMap()
                    user.put("name",document.data.get("name").toString())
                    user.put("profile_img",document.data.get("profile_img").toString())
                    UserList.add(user)
                    Log.d("UserList", UserList.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception) }
    }
    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}
