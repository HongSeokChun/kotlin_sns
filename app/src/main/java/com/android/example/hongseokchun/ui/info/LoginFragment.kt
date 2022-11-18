package com.android.example.hongseokchun.ui.info

import android.util.Log
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private lateinit var auth: FirebaseAuth

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("none2")

        auth = Firebase.auth

//        로그인 되어있는지 확인
//        val currentUser = auth.currentUser
//        if(currentUser != null) {
//            navController.navigate(R.id.action_loginFragment_to_peedFragment)
//        }
    }

    override fun initDataBinding() {
        super.initDataBinding()


        //회원가입 버튼 이벤트
        binding.signUpBtn.setOnClickListener{
        }
    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    private fun setName(email:String) {
        val db = Firebase.firestore
        // 앱 저장소에 이름 저장
        db.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                var name = document.get("name").toString()
//                prefs.setString("name", name)
            }
            .addOnFailureListener{
                Log.d("error LoginFragment", "null")
            }
    }

}