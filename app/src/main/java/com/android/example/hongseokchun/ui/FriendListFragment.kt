package com.android.example.hongseokchun.ui


import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentFriendListBinding
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URL

class FriendListFragment : BaseFragment<FragmentFriendListBinding>(R.layout.fragment_friend_list) {
    private lateinit var friendAdapter: FriendAdapter
    val db = Firebase.firestore

    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initStartView() {
        super.initStartView()
    }


    override fun initDataBinding() {
        super.initDataBinding()
        friendAdapter= FriendAdapter(ArrayList())

        viewModel.getUserFriends()

        binding.recyclerView.adapter=friendAdapter

        viewModel.userFriendsLiveData.observe(viewLifecycleOwner) { itemList ->
            friendAdapter.itemList = itemList
            val n = itemList.size;
            binding.friendNum.text = "친구 ${n}명"
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
                viewModel.userFriendsLiveData.observe(viewLifecycleOwner) { itemList ->
                    for (item in itemList){
                        if(item.get("name")?.contains(searchString) == true){
                            searchItemList.add(item)
                        }
                    }
                    friendAdapter.itemList = searchItemList

                    if (binding.etSearchFriend.text.isEmpty()) {
                        viewModel.userFriendsLiveData.observe(viewLifecycleOwner) { itemList ->
                            friendAdapter.itemList = itemList
                        }
                    }
                }
            }
        })


        //친구삭제
        friendAdapter.setItemClickListener(object: FriendAdapter.OnItemClickListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                val friendName = friendAdapter.itemList[position].get("name")
                val dataOrigin = friendAdapter.itemList[position]

                    db.collection("users").document("hongseokchun@naver.com")
                        .update("friends", FieldValue.arrayRemove(dataOrigin))
                    viewModel.getUserFriends()

            }
        })
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}