package com.android.example.hongseokchun.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.hongseokchun.model.Posts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PeedRepository() {
    private var posts: MutableSet<Posts> = mutableSetOf<Posts>()

    fun getPeedData(friendNames: ArrayList<String>): MutableLiveData<List<Posts>> {
        val db = Firebase.firestore
        var mutableData = MutableLiveData<List<Posts>>()
        posts.clear()
        for(name in friendNames) {
            Log.d("friendName",name)
            db.collection("users").document(name).collection("Post").get()
                .addOnSuccessListener { documentSnapshot ->
                    for (document in documentSnapshot) {
                        val data = document.toObject<Posts>()
                        if (data != null) {
                            //mutableData.value = listOf(data)
                            Log.d("Peeddata", data.toString())
                            posts.add(data)
                        }
                    }

                    mutableData.value = posts.toList()

                    Log.d("posts.toString", posts.toString())
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }


        }
        Log.d("peedRepomutableData",mutableData.value.toString())
        Log.d("peedRepomutableData",mutableData.toString())
        return mutableData
    }

    fun <T> clone(original: Set<T>): Set<T> {
        return original.toSet()
    }
}