package com.android.example.hongseokchun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.model.PostContent
import com.android.example.hongseokchun.repository.PostRepository

class PostViewModel : ViewModel(){
    private val _postLiveData: MutableLiveData<ArrayList<PostContent>>
            = MutableLiveData()
    val postLiveData: LiveData<ArrayList<PostContent>>
        get() = _postLiveData
    private val repo = PostRepository()

    fun getPosts() {
        repo.getData().observeForever{
            _postLiveData.value = it.postArray
            Log.d("ingred vm", _postLiveData.value.toString())
        }
    }


}