package com.android.example.hongseokchun.ui.info

import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {
    private lateinit var auth : FirebaseAuth

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("none2")
    }

    override fun initDataBinding() {
        super.initDataBinding()
        auth = FirebaseAuth.getInstance()

    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}
