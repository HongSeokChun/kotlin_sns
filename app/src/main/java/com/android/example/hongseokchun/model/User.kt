package com.android.example.hongseokchun.model

data class User(
    val email: String = "",
    val name: String = "",
    val date: String= "",
    val question: String ="",
    val answer: String="",
    val friends: ArrayList<HashMap<String,String>> = ArrayList(),
    val profileUrl: String = "",
    val following :ArrayList<HashMap<String,String>> = ArrayList(),
    val follower : ArrayList<HashMap<String,String>> = ArrayList(),
    val post : ArrayList<Posts> = ArrayList()
)
