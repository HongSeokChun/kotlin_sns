package com.android.example.hongseokchun.ui.peed

import android.os.Bundle
import android.os.Handler
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentPeedBinding
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.model.PostContent
import com.android.example.hongseokchun.ui.PeedAdapter
import com.android.example.hongseokchun.viewmodel.PeedViewModel
import com.android.example.hongseokchun.ui.mypage.FriendAdapter

import com.android.example.hongseokchun.viewmodel.PostViewModel
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PeedFragment : BaseFragment<FragmentPeedBinding>(R.layout.fragment_peed) {
    private lateinit var peedAdapter: PeedAdapter
    private lateinit var swipe: SwipeRefreshLayout
    val db = Firebase.firestore

    private val friendViewModel by lazy {
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
        peedAdapter = PeedAdapter(ArrayList())
        var friendsNames: ArrayList<String> = ArrayList()
        swipe = binding.swipe

        viewModel.getPosts()
//        friendViewModel.getUserFriends()

        var myName = "hong@hong.hong";
        friendViewModel.getUserFriends()
        friendViewModel.userFriendsLiveData.observe(viewLifecycleOwner) { itemList ->
            if (itemList != null) {
                for (friend in itemList)
                    friend.get("name")?.let { friendsNames.add(it) }
                friendsNames.add(myName);
                peedViewModel.getPosts(friendsNames)
                Log.d("friendsNames", friendsNames.toString())
            }
        }

        peedViewModel.peedLiveData.observe(viewLifecycleOwner) { itemList ->
            peedAdapter.itemList = itemList
            Log.d("peeditemList", itemList.toString())
        }

        binding.recyclerview.adapter = peedAdapter
        binding.addPost.setOnClickListener{
            navController.navigate(R.id.action_peedFragment_to_editPostFragment)
        }

        binding.notification.setOnClickListener {
            navController.navigate(R.id.action_peedFragment_to_notificationFragment)
        }



        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

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