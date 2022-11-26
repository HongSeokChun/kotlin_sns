package com.android.example.hongseokchun.ui.peed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentNotificationBinding
import com.android.example.hongseokchun.databinding.FragmentNotificationListBinding
import com.android.example.hongseokchun.databinding.FriendListViewBinding
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.User
import com.android.example.hongseokchun.ui.friend.FriendAdapter
import com.android.example.hongseokchun.ui.mypage.MyPageAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class NotificationFragment : BaseFragment<FragmentNotificationListBinding>(R.layout.fragment_notification_list) {
    var alarmDTOList : ArrayList<AlarmDTO> = arrayListOf()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("알림")

    }

    override fun initDataBinding() {
        super.initDataBinding()

        val uid = prefs.getString("email","")

        FirebaseFirestore.getInstance().collection("users").document(uid).collection("Alarm")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("alarmDTO",document.toString())
                    val alarmDto = document.toObject<AlarmDTO>()
                    alarmDTOList.add(alarmDto)
                    notificationAdapter = NotificationAdapter(alarmDTOList)
                    binding.recyclerView.adapter = notificationAdapter
                }
                alarmDTOList.sortByDescending { it.timestamp }
                Log.d("alarmDTOList",alarmDTOList.size.toString())
            }
            .addOnFailureListener { exception ->
            }


    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }


}
