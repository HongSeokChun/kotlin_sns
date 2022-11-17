package com.android.example.hongseokchun.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.example.hongseokchun.model.PostContent
import com.android.example.hongseokchun.model.Posts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class PeedRepository(val friendName: HashMap<String, String>) {
    // private lateinit var friendPosts: ArrayList<PostContent>

    private var data: Posts? = null

    fun getPeedData(): MutableLiveData<List<PostContent>> {
        val db = Firebase.firestore
        val mutableData = MutableLiveData<List<PostContent>>()
        var tempList = mutableListOf<PostContent>()

        friendName.get("name")?.let {
            db.collection("Posts").document(it).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<Posts>()
                    if (data != null) {
                        Log.d("peedRepoData", data.toString())
                        Log.d("peedRepoData.PostArray", data!!.postArray.toString())
                        mutableData.value = data.postArray
                        Log.d("tempListInListener", tempList.toString())

                    }

                    Log.d("posts repo", data.toString())
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }


//        if(friendNames.value != null) {
//            for (friendname in friendNames.value!!) {
//                Log.d("friends name", friendname.toString())
//                friendname.get("name")?.let {
//                    db.collection("Posts").document(it).get()
//                        .addOnSuccessListener { documentSnapshot ->
//                            data = documentSnapshot.toObject<Posts>()
//                            if (data != null) {
//                                for (postContent in data!!.postArray) {
//                                    Log.d("peedRepoData", data.toString())
//                                    Log.d("peedRepoData.PostArray", data!!.postArray.toString())
//                                    mutableData.value?.add(postContent)
//                                    Log.d("tempListInListener", tempList.toString())
//                                }
//                            }
//                            Log.d("peedRepo", data.toString())
//                        }
//                        .addOnFailureListener { exception ->
//                            Log.d(ContentValues.TAG, "get failed with ", exception)
//                        }
//                }
//            }
//        }


        //temp post 만들어서 apppend 다하고 마지막에 mutable data에 전이

//        db.collection("Posts").document("hong@hong.hong").get()
//            .addOnSuccessListener { documentSnapshot ->
//                val data = documentSnapshot.toObject<Posts>()
//                mutableData.value=data!!
//                Log.d("posts repo",data.toString())
//            }
//            .addOnFailureListener { exception ->
//                Log.d(ContentValues.TAG, "get failed with ", exception)
//            }
//        Log.d("tempList",tempList.toString())
//        mutableData.value = tempList
        Log.d("peedRepoMutableData",mutableData.value.toString())


        return mutableData
    }
}