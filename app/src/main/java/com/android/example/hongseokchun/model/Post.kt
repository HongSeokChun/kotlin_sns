package com.android.example.hongseokchun.model

data class Posts (

    val postAdmin : String = "",
    val imageNames : ArrayList<String> = ArrayList(),
    val comments  : HashMap<String,String> = HashMap(),
    val uploadDate: String="",
    val like: Int =0,
    val mainText : String = "",

)

//data class Posts(
//    val postArray : ArrayList<PostContent> = ArrayList()
//)