package com.android.example.hongseokchun.ui.info

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentMemberFindLoginPwBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserFindLoginPwFragment  : BaseFragment<FragmentMemberFindLoginPwBinding>(R.layout.fragment_member_find_login_pw) {
    private val db = Firebase.firestore

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("비밀번호 찾기")
    }

    override fun initDataBinding() {
        super.initDataBinding()

        binding.btnFindPW2.setOnClickListener {
            val email = binding.tvEmail.text.toString()

            if(email.isEmpty()){
                Toast.makeText(context,"이메일을 입력해주세요", Toast.LENGTH_LONG).show()
            }


            db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() == 0){
                    AlertDialog.Builder(context).run {
                        setTitle("비밀번호 찾기")
                        setMessage("가입정보가 없습니다. 다시 한번 확인해주세요")
                        setPositiveButton("확인",null)
                        show()
                    }
                    binding.btnFindPW2.setText("")
                }
                for (document in documents) {
                    Firebase.auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                AlertDialog.Builder(context).run {
                                    setTitle("비밀번호 찾기")
                                    setMessage("이메일주소로 비밀번호 변경링크를 보냈습니다.")
                                    setPositiveButton("확인",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            navController.navigate(R.id.action_userFindLoginPwFragment_to_loginFragment)
                                        })
                                    show()
                                }
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}
