package com.android.example.hongseokchun.ui.peed

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentPeedBinding
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.PeedAdapter
import com.android.example.hongseokchun.ui.friend.FriendAdapter
import com.android.example.hongseokchun.viewmodel.PeedViewModel

import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PeedFragment : BaseFragment<FragmentPeedBinding>(R.layout.fragment_peed) {
    private lateinit var peedAdapter: PeedAdapter
    private lateinit var swipe: SwipeRefreshLayout
    val db = Firebase.firestore
    val currentUserEmail = Firebase.auth.currentUser?.email
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
        swipe = binding.swipe //당겨 새로고침
        peedAdapter = PeedAdapter(ArrayList())

        var friendsNames: ArrayList<String> = ArrayList()

        //친구+내 이메일로 post 가져오기
        userViewModel.getflollowingUsers()
        userViewModel.followingUserLiveData.observe(viewLifecycleOwner) { itemList ->
            if (itemList != null) {
                for (friend in itemList)
                    friend.get("email")?.let { friendsNames.add(it) }
                friendsNames.add(currentUserEmail!!);
                peedViewModel.getPosts(friendsNames)//친구 + 나의 Post 가져오기
                Log.d("friendsNames", friendsNames.toString())
            }
        }

        //PeedAdapter에 피드용 List<Post> 넘기기
        peedViewModel.peedLiveData.observe(viewLifecycleOwner) { itemList ->
            peedAdapter.itemList = itemList
        }


        binding.notification.setOnClickListener {
            navController.navigate(R.id.action_peedFragment_to_notificationFragment)
        }

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = peedAdapter


        peedAdapter.setItemClickListener(object: PeedAdapter.OnItemClickListener {
            override fun onClick(btn: String, position: Int) {
                navController.navigate(R.id.action_peedFragment_to_friendPageFragment)
            }
        })
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


}
