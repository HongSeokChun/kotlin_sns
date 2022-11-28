package com.android.example.hongseokchun.ui.friend

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentFindUserBinding
import com.android.example.hongseokchun.model.FollowUser
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FindUserFragment : BaseFragment<FragmentFindUserBinding>(R.layout.fragment_find_user) {
    private lateinit var friendAdapter: FriendAdapter
    val db = Firebase.firestore
    val UserList : ArrayList<HashMap<String,String>> = ArrayList()
    lateinit var user : HashMap<String,String>
    val loginUser = prefs.getString("email","null")

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("view")
    }

    override fun initDataBinding() {
        super.initDataBinding()

        getUserList()


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


                if (binding.etSearchFriend.text.isEmpty()) {
                    searchItemList.clear()
                    binding.recommedFriend.visibility= View.VISIBLE
                    friendAdapter = FriendAdapter(UserList)
                    binding.recyclerView.adapter = friendAdapter

                }else {
                    binding.recommedFriend.visibility = View.INVISIBLE
                    for (data in UserList) {
                        val name = data.get("name");
                        if (name != null) {
                            if (searchString in name) {
                                searchItemList.add(data)
                            }
                        }
                    }

                    friendAdapter = FriendAdapter(searchItemList)
                    binding.recyclerView.adapter = friendAdapter
                }


            }
        })





    }


    // 전체 유저 목록 가져오기
    fun getUserList(){
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                UserList.clear()
                for (document in result) {
                    user = HashMap()
                    user.put("name",document.data.get("name").toString())
                    user.put("profile_img",document.data.get("profile_img").toString())
                    user.put("email",document.data.get("email").toString())
                    UserList.add(user)
                    Log.d("UserList", UserList.toString())
                }
                friendAdapter= FriendAdapter(UserList)

                binding.recyclerView.adapter=friendAdapter

                //팔로우,팔로잉 버튼 클릭
                friendAdapter.setItemClickListener(object: FriendAdapter.OnItemClickListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onClick(btn: String, position: Int) {
                        // 클릭 시 이벤트 작성
                        val friendName = friendAdapter.itemList[position].get("name")
                        val dataOrigin = friendAdapter.itemList[position]

                        if(btn =="팔로우") {
                            // 원래 친구목록에 있으면 삭제
                            db.collection("users").document(prefs.getString("email","null"))
                                .update("following", FieldValue.arrayRemove(dataOrigin))
                            changeFollowState(false)
                        }
                        else if(btn=="팔로잉"){
                            //없으면 추가
                            db.collection("users").document(prefs.getString("email","null"))
                                .update("following", FieldValue.arrayUnion(dataOrigin))
                            changeFollowState(true)

                        }
                        else{
                            navController.navigate(R.id.action_findUserFragment_to_friendPageFragment)
                        }

                    }

                })
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception) }



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

}
