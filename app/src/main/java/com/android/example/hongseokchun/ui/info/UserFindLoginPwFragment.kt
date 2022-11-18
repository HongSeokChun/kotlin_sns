package com.android.example.hongseokchun.ui.info

import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentMemberFindLoginPwBinding

class UserFindLoginPwFragment  : BaseFragment<FragmentMemberFindLoginPwBinding>(R.layout.fragment_member_find_login_pw) {

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("none2")
    }

    override fun initDataBinding() {
        super.initDataBinding()


    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}
