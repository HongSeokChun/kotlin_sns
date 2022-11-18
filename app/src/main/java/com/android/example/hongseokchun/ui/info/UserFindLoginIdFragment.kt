package com.android.example.hongseokchun.ui.info

import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentMemberFindLoginIdBinding
import com.android.example.hongseokchun.databinding.FragmentSignUpBinding

class UserFindLoginIdFragment : BaseFragment<FragmentMemberFindLoginIdBinding>(R.layout.fragment_member_find_login_id) {

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

