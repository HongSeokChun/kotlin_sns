package com.android.example.hongseokchun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.hongseokchun.repository.UserRepository

class UserViewModel : ViewModel(){

    private val _followingUserLiveData: MutableLiveData<ArrayList<HashMap<String,String>>>
            = MutableLiveData()
    val followingUserLiveData: LiveData<ArrayList<HashMap<String,String>>>
        get() = _followingUserLiveData
    private val repo = UserRepository()

    fun getflollowingUsers() {
        repo.getData().observeForever{
            _followingUserLiveData.value = it.following
            Log.d("following user", _followingUserLiveData.value.toString())
        }
    }

    private val _followerUserLiveData: MutableLiveData<ArrayList<HashMap<String,String>>>
            = MutableLiveData()
    val followerUserLiveData: LiveData<ArrayList<HashMap<String,String>>>
        get() = _followerUserLiveData

    fun getflollowerUsers() {
        repo.getData().observeForever{
            _userFriendsLiveData.value = it.friends
            // .sortWith(compareBy<Ingredient>{it.added}.thenBy{it.name})
            Log.d("user vm", _userFriendsLiveData.value.toString())
            _followerUserLiveData.value = it.follower
            Log.d("follower user", _followerUserLiveData.value.toString())
        }
    }


}