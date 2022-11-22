package com.android.example.hongseokchun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.repository.PeedRepository

class PeedViewModel() : ViewModel() {
    private val _peedLiveData: MutableLiveData<List<Posts>> = MutableLiveData()
    val peedLiveData: LiveData<List<Posts>>
        get() = _peedLiveData
    private val repo = PeedRepository()

    fun getPosts(friendNames: ArrayList<String>){
        repo.getPeedData(friendNames).observeForever{
            _peedLiveData.value = it
            Log.d("peedLiveData", _peedLiveData.value.toString())
        }
    }
}


