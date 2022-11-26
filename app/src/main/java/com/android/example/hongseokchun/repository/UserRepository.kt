package com.android.example.hongseokchun.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.hongseokchun.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UserRepository {
    fun getData(): LiveData<User> {
        val db = Firebase.firestore
        val mutableData = MutableLiveData<User>()

        db.collection("users").document("hongseokchun@naver.com").get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                mutableData.value=data!!
                Log.d("friend repo",data.toString())
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        return mutableData
    }

    fun getCurrentUserData(currrentUserEmail: String): LiveData<User> {
        Log.d("currentUserEmail2",currrentUserEmail)
        val db = Firebase.firestore
        val mutableData = MutableLiveData<User>()

        db.collection("users").document(currrentUserEmail).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                mutableData.value=data!!
                Log.d("friend repo",data.toString())
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        return mutableData
    }
}