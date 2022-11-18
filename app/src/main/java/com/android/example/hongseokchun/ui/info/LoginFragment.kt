package com.android.example.hongseokchun.ui.info

import android.util.Log
import android.util.Patterns
import android.widget.Toast
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
    private var rememberUser : Boolean = false

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("none2")

        auth = Firebase.auth

//        로그인 되어있는지 확인
        if (rememberUser == true){
            val currentUser = auth.currentUser
//        if(currentUser != null) {
//            navController.navigate(R.id.action_loginFragment_to_peedFragment)
//        }
        }
    }

    override fun initDataBinding() {
        super.initDataBinding()


        //로그인 버튼 이벤트
        binding.btnLogin.setOnClickListener{
        val ToastText = mutableListOf<String>()
        var isGoToLogin = true
        val email = binding.tvEmail.text.toString()
        val password = binding.tvPw.text.toString()

        // 유효성 검사
        if (email.isEmpty() and !password.isEmpty()) {
            Toast.makeText(context, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
            isGoToLogin = false
        }

        if (password.isEmpty() and !email.isEmpty()) {
            Toast.makeText(context, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            isGoToLogin = false
        }

        if (email.isEmpty() and password.isEmpty()){
            Toast.makeText(context, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            isGoToLogin = false
        }

        if (!email.isEmpty() and !password.isEmpty() and !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
            binding.tvEmail.setText("")
            isGoToLogin = false
        }



        //로그인
        if(isGoToLogin){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if(binding.checkId.isChecked){
                            //preferences에 유저 이메일 저장
                            // prefs.setString("email",email)
                            rememberUser = true

                        }
                        setName(email)
                        Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()

                        navController.navigate(R.id.action_loginFragment_to_peedFragment)
                    } else {
                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

        //아이디찾기 버튼
        binding.btnFindID1.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_userFindLoginIdFragment)
        }

        //비밀번호찾기 버튼
        binding.btnFindPW1.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_userFindLoginPwFragment)
        }

        // 회원가입 버튼
        binding.signUpBtn.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
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