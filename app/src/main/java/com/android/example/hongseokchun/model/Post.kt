package com.android.example.hongseokchun.model

import android.net.Uri

data class Post (
    val imageNames : ArrayList<String> = ArrayList(),
    val message : String ="",
    val uploadDate: String=""
)