package com.android.example.hongseokchun.ui.friend

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentFollowerListBinding
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FollowerUserFragment  : BaseFragment<FragmentFollowerListBinding>(R.layout.fragment_follower_list) {
    private lateinit var friendAdapter: FriendAdapter
    val db = Firebase.firestore

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

    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}