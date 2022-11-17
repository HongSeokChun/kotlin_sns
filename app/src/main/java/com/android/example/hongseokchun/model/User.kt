package com.android.example.hongseokchun.model

data class User(
    val email: String = "",
    val name: String = "",
    val birth: String= "",
    val question: String ="",
    val answer: String="",
    val friends: ArrayList<HashMap<String,String>> = ArrayList(),
    val profile_img: String = "",
    val following :ArrayList<String> = ArrayList(),
    val follower : ArrayList<String> = ArrayList(),
    val post : ArrayList<Posts> = ArrayList()
)
