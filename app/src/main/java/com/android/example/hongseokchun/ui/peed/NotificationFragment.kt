package com.android.example.hongseokchun.ui.peed

import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentNotificationBinding
import com.android.example.hongseokchun.databinding.FragmentNotificationListBinding

class NotificationFragment : BaseFragment<FragmentNotificationListBinding>(R.layout.fragment_notification_list) {

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("알림")
    }

    override fun initDataBinding() {
        super.initDataBinding()


    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}
