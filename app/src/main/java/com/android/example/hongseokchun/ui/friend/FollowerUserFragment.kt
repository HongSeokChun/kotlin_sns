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
            binding.friendNum.text = "${n}???"
            Log.d("recipee",itemList.toString())
        }


        //????????????
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


        //?????????,????????? ?????? ??????
        friendAdapter.setItemClickListener(object: FriendAdapter.OnItemClickListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onClick(btn: String, position: Int) {
                // ?????? ??? ????????? ??????
                val friendName = friendAdapter.itemList[position].get("name")
                val dataOrigin = friendAdapter.itemList[position]

                if(btn =="?????????") {
                    // ?????? ??????????????? ????????? ??????
                    db.collection("users").document(loginUser)
                        .update("following", FieldValue.arrayRemove(dataOrigin))
                    // ?????? DB??? ??????
                    changeFollowState(false)
                }
                else if(btn=="?????????") {
                    //????????? ??????
                    db.collection("users").document(loginUser)
                        .update("following", FieldValue.arrayUnion(dataOrigin))
                    // ?????? DB??? ??????
                    changeFollowState(true)

                    // ????????? ??????
                    var alarmDTO = AlarmDTO()
                    alarmDTO.destinationUid =prefs.getString("watchUser", "null")
                    alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
                    alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
                    alarmDTO.kind = 0
                    alarmDTO.message ="${prefs.getString("name","")}?????? ????????? ???????????????."
                    alarmDTO.timestamp = System.currentTimeMillis()

                    FirebaseFirestore.getInstance().collection("users").document(prefs.getString("watchUser", "null"))
                        .collection("Alarm").document().set(alarmDTO)


                }
                else{
                    // ?????? ????????? ??????
                    navController.navigate(R.id.action_friendFragment_to_friendPageFragment)
                }
            }

        })
    }

    fun changeFollowState(following:Boolean){
       val myProfile = FollowUser(loginUser, prefs.getString("name","null"), prefs.getString("profileImg","null"))

        if(following) {
            // ?????? ????????? ????????? ??? ?????? ????????? -> ?????????
            db.collection("users").document(prefs.getString("watchUser", "null"))
                .update("follower", FieldValue.arrayUnion(myProfile))

        }else {
            // ?????? ????????? ????????? ??? ?????? ?????? -> ????????? ??????
            db.collection("users").document(prefs.getString("watchUser", "null"))
                .update("follower", FieldValue.arrayRemove(myProfile))
        }
    }
    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}