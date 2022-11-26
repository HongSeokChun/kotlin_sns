package com.android.example.hongseokchun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.repository.UserRepository

class UserViewModel : ViewModel(){

    private val _followingUserLiveData: MutableLiveData<ArrayList<HashMap<String,String>>>
            = MutableLiveData()

//    val userFriendsLiveData: LiveData<ArrayList<HashMap<String,String>>>
//        get() = _followingUserLiveData

    private val _userLiveData: MutableLiveData<User>
            = MutableLiveData()
    val userLiveData: LiveData<User>
        get() = _userLiveData

    val followingUserLiveData: LiveData<ArrayList<HashMap<String,String>>>
        get() = _followingUserLiveData
    private val repo = UserRepository()

    fun getflollowingUsers() {
        repo.getData().observeForever{
            _followingUserLiveData.value = it.following
            Log.d("following user", _followingUserLiveData.value.toString())
        }
    }

    fun getflollowerUsers() {
        repo.getData().observeForever{
            _followerUserLiveData.value = it.follower
            Log.d("following user", _followerUserLiveData.value.toString())
        }
    }

    private val _followerUserLiveData: MutableLiveData<ArrayList<HashMap<String,String>>>
            = MutableLiveData()
    val followerUserLiveData: LiveData<ArrayList<HashMap<String,String>>>
        get() = _followerUserLiveData

    fun getUser(currentUserEmail: String){
        repo.getCurrentUserData(currentUserEmail).observeForever{
            _userLiveData.value = it
            // .sortWith(compareBy<Ingredient>{it.added}.thenBy{it.name})
            _followerUserLiveData.value = it.follower
        }
    }


}