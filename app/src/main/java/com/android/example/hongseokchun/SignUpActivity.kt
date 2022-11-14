package com.android.example.hongseokchun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.example.hongseokchun.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : AppCompatActivity() {
    lateinit var binding : ActivitySignUpBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
    }


}