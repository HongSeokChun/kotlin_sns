package com.android.example.hongseokchun.ui.peed

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentPeedBinding
import com.android.example.hongseokchun.ui.PeedAdapter
import com.android.example.hongseokchun.viewmodel.PeedViewModel

import com.android.example.hongseokchun.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PeedFragment : BaseFragment<FragmentPeedBinding>(R.layout.fragment_peed) {
    private lateinit var peedAdapter: PeedAdapter
    private var friendsNames: ArrayList<String> = ArrayList()
    //private lateinit var peedViewModel: PeedViewModel
    val db = Firebase.firestore

//    private val viewModel by lazy {
//        ViewModelProvider(this)[PostViewModel::class.java]
//    }
    private val friendViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    private lateinit var peedViewModel: PeedViewModel

    override fun initStartView() {
        super.initStartView()
    }

    override fun initDataBinding() {
        super.initDataBinding()
        peedAdapter = PeedAdapter(ArrayList())

        //viewModel.getPosts()

        friendViewModel.getUserFriends()
        friendViewModel.userFriendsLiveData.observe(viewLifecycleOwner) {
//            for (friendsMap in it) {
//                friendsNames.add(friendsMap.get("name").toString())
//                Log.d("frag firendsName", friendsNames.toString())
//            }

        }
        peedViewModel = ViewModelProvider(this, PeedViewModel.Factory(friendViewModel.userFriendsLiveData)).get(PeedViewModel::class.java)



        binding.recyclerview.adapter = peedAdapter

//
//        viewModel.postLiveData.observe(viewLifecycleOwner) { itemList ->
//            peedAdapter.itemList = itemList
//            Log.d("recipee", itemList.toString())
//    }

        peedViewModel.getPosts()
        peedViewModel.peedLiveData.observe(viewLifecycleOwner){
            peedAdapter.itemList = it
            Log.d("recipee", it.toString())
        }
//        viewModel.postLiveData.observe(viewLifecycleOwner) { itemList ->
//            peedAdapter.itemList = itemList
//            Log.d("recipee", itemList.toString())
//
//        }

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
//        binding.recyclerview.adapter = peedAdapter


    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}