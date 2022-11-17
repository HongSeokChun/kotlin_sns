package com.android.example.hongseokchun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.repository.UserRepository

class UserViewModel : ViewModel(){
    private val _userFriendsLiveData: MutableLiveData<ArrayList<HashMap<String,String>>>
            = MutableLiveData()
    val userFriendsLiveData: LiveData<ArrayList<HashMap<String,String>>>
        get() = _userFriendsLiveData
    private val repo = UserRepository()

    fun getUserFriends() {
        repo.getData().observeForever{
            _userFriendsLiveData.value = it.friends
            // .sortWith(compareBy<Ingredient>{it.added}.thenBy{it.name})
            Log.d("user vm", _userFriendsLiveData.value.toString())
        }
    }


}