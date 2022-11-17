package com.android.example.hongseokchun.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.hongseokchun.model.Posts
import com.android.example.hongseokchun.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class PostRepository {
    fun getData(): LiveData<Posts> {
        val db = Firebase.firestore
        val mutableData = MutableLiveData<Posts>()

        db.collection("Posts").document("hong@hong.hong").get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<Posts>()
                mutableData.value=data!!
                Log.d("posts repo",data.toString())
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        return mutableData
    }
}