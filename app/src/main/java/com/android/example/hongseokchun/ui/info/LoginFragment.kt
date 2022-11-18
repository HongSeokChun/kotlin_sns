package com.android.example.hongseokchun.ui.info

import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentLoginBinding
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun initStartView() {
        super.initStartView()

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

}