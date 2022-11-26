package com.android.example.hongseokchun.model

data class Posts (

    val postAdmin : String = "",
    val imageNames : ArrayList<String> = ArrayList(),
    val uploadDate: String="",
    var like: Int = 0,
    val mainText : String = "",
    val commentCount : Int = 0,
    val postAdminProfile: String = "",
    val likes: ArrayList<String> = ArrayList()
)

//data class Posts(
//    val postArray : ArrayList<PostContent> = ArrayList()
//)