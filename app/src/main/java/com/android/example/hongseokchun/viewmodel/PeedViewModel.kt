package com.android.example.hongseokchun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.hongseokchun.model.PostContent
import com.android.example.hongseokchun.repository.PeedRepository

class PeedViewModel(friendNames: LiveData<ArrayList<HashMap<String, String>>>) : ViewModel(){
    private val _peedLiveData: MutableLiveData<MutableList<PostContent>>
            = MutableLiveData()
    val peedLiveData: LiveData<MutableList<PostContent>>
        get() = _peedLiveData
    //private val repo = PeedRepository(friendNames)

    fun getPosts(friendNames: LiveData<ArrayList<HashMap<String, String>>>) {
//        repo.getPeedData().observeForever{
//            _peedLiveData.value = it
//            Log.d("peed vm", _peedLiveData.value.toString())
//        }
//        if (friendNames.value != null)
//            for(friendName in friendNames.value!!) {
//                PeedRepository(friendName).getPeedData().observeForever(){
//                    _peedLiveData.value = it as MutableList<PostContent>?
////            Log.d("peed vm", _peedLiveData.value.toString())
//                }
//            }
    }

    class Factory(val friendNames: LiveData<ArrayList<HashMap<String, String>>>) : ViewModelProvider.Factory {
        override fun <T : ViewModel > create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PeedViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PeedViewModel(friendNames) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}


