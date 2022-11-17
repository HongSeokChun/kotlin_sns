package com.android.example.hongseokchun.model

data class PostContent (
    val postID : String = "",
    val imageNames : ArrayList<String> = ArrayList(),
    val comments  : HashMap<String,String> = HashMap(),
    val uploadDate: String="",
    val like: String ="",
    val mainText : String = "",
    val postAdmin : String = ""
)

data class Posts(
    val postArray : ArrayList<PostContent> = ArrayList()
)