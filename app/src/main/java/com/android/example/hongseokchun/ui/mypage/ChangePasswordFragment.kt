package com.android.example.hongseokchun.ui.mypage

import android.util.Patterns
import android.widget.Toast
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>(R.layout.fragment_change_password) {
    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("비밀번호 변경")
    }

    override fun initDataBinding() {
        super.initDataBinding()
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnSetPassword.setOnClickListener {
            var isGoToChange = true
            var Check = true
            val EmptyString  = emptyList<String>().toMutableList()
            val emailCheck = binding.etId.text.toString()
            val newPassword = binding.etPassword.text.toString()
            val newPasswordCheck = binding.etPasswordCheck.text.toString()

            val user = Firebase.auth.currentUser

            //유효성 검사
            if(emailCheck.isEmpty())
                EmptyString.add("이메일")
            if(newPassword.isEmpty())
                EmptyString.add("비밀번호")
            if(newPasswordCheck.isEmpty())
                EmptyString.add("비밀번호 확인")

            if(emailCheck.isEmpty() or newPassword.isEmpty() or newPasswordCheck.isEmpty()){
                Toast.makeText(context, EmptyString.toString()+" 을/를 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToChange = false
                Check = false
            }

            if (Check and !Patterns.EMAIL_ADDRESS.matcher(emailCheck).matches()) {
                Toast.makeText(context, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                binding.etId.setText("")
                isGoToChange = false
                Check = false
            }

            if(Check and (user?.email != emailCheck)){
                Toast.makeText(context, "이메일이 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                isGoToChange = false
                Check = false
            }

            if (Check and (newPassword != newPasswordCheck)){
                Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                isGoToChange = false
            }


            //비밀번호 변경
            if (isGoToChange) {
                user!!.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()
                            navController.navigate(R.id.action_changePasswordFragment_to_loginFragment)
                        }
                        else{
                            Toast.makeText(context, "비밀번호 실패.", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}