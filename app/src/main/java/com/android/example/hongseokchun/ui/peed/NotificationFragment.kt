package com.android.example.hongseokchun.ui.peed

import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentNotificationBinding

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {

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
