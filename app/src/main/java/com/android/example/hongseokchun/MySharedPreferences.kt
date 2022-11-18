package com.android.example.hongseokchun

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

class MySharedPreferences(context : Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun readAll(){ // 저장되어 있는 데이터 Log로 출력
        Log.d("prefs readAll", prefs.all.toString())
    }

    fun getString(key: String, defValue: String): String { // 데이터 불러오기
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) { // 데이터 저장
       prefs.edit().putString(key, str).apply()
    }

    fun remove(key: String){    //데이터 삭제
        prefs.edit().remove(key).apply()
    }
    fun removeAll(){
        prefs.edit().clear()
    }
}