package com.android.example.hongseokchun.ui.peed

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentPeedBinding
import com.android.example.hongseokchun.model.PostContent
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.ui.PeedAdapter

import com.android.example.hongseokchun.viewmodel.PostViewModel
import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PeedFragment: BaseFragment<FragmentPeedBinding>(R.layout.fragment_peed) {
    private lateinit var peedAdapter: PeedAdapter
    private lateinit var friensPost : ArrayList<PostContent>
    val db = Firebase.firestore

    private val viewModel by lazy {
        ViewModelProvider(this)[PostViewModel::class.java]
    }
    private val friendViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }
    override fun initStartView() {
        super.initStartView()
    }

    override fun initDataBinding() {
        super.initDataBinding()
        peedAdapter = PeedAdapter(ArrayList())

        viewModel.getPosts()
        friendViewModel.getUserFriends()


        binding.recyclerview.adapter = peedAdapter

        //이렇게 짜면 안되는데.. 일단은
        viewModel.postLiveData.observe(viewLifecycleOwner) { itemList ->
//            friendViewModel.userFriendsLiveData.observe(viewLifecycleOwner) { friendList ->
//                for (post in itemList) {
//                    for (friend in friendList)
//                        if (post.postAdmin == friend.get("name"))
//                            friensPost.add(post)
//                }
//        }

            peedAdapter.itemList = itemList
            Log.d("recipee", itemList.toString())

        }

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
//        binding.recyclerview.adapter = peedAdapter


    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}