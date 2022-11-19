package com.android.example.hongseokchun.ui.info

import android.util.Patterns
import android.widget.Toast
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentSignUpBinding
import com.android.example.hongseokchun.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("회원가입")
        auth = Firebase.auth
    }

    override fun initDataBinding() {
        super.initDataBinding()
        auth = FirebaseAuth.getInstance()

        binding.btnSingup.setOnClickListener {
            var isGoToJoin = true
            var EmptyCheck = true
            val EmptyString  = emptyList<String>().toMutableList()

            val email = binding.tvEmail.text.toString()
            val name = binding.tvName.text.toString()
            val password = binding.tvPw.text.toString()
            val passwordCheck=binding.tvPwcheck.text.toString()
            val birth = binding.tvBorndate.text.toString()
            val findIdQ = binding.tvFindIDA.text.toString()
            val findIdA = binding.tvFindIDQ.text.toString()


            // 유효성 검사
            if(name.isEmpty())
                EmptyString.add("이름")
            if(email.isEmpty())
                EmptyString.add("이메일")
            if(password.isEmpty())
                EmptyString.add("비밀번호")
            if(passwordCheck.isEmpty())
                EmptyString.add("비밀번호 확인")
            if(birth.isEmpty())
                EmptyString.add("생년월일")
            if(findIdQ.isEmpty())
                EmptyString.add("아이디찾기 질문")
            if(findIdA.isEmpty())
                EmptyString.add("아이디찾기 답변")

            if(name.isEmpty() or email.isEmpty() or password.isEmpty() or passwordCheck.isEmpty() or birth.isEmpty() or
                    findIdA.isEmpty() or findIdA.isEmpty()){
                Toast.makeText(context, "$EmptyString 을/를 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
                EmptyCheck = false
            }

            if (EmptyCheck and (password != passwordCheck)) {
                Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if (EmptyCheck and (password == passwordCheck) and !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show()
                binding.tvEmail.setText("")
                isGoToJoin = false
            }

            //아이디와 비밀번호로 user 생성
            if (isGoToJoin) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "회원가입 성공", Toast.LENGTH_LONG).show()

                            //MyApplication.prefs.setString("email",email)

                            val data = User(email,name,birth,findIdQ,findIdA)
                            db.collection("users").document(email).set(data)

                            navController.navigate(R.id.action_signUpFragment_to_loginFragment)
                        } else {
                            Toast.makeText(context, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}
