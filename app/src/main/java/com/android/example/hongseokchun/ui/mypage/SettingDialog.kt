package com.android.example.hongseokchun.ui.mypage

import com.android.example.hongseokchun.MyApplication.Companion.prefs
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseBottomDialogFragment
import com.android.example.hongseokchun.databinding.DialogSettingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingDialog : BaseBottomDialogFragment<DialogSettingBinding>(R.layout.dialog_setting) {
    private var auth = Firebase.auth

    override fun initDataBinding() {
        super.initDataBinding()

    }
    override fun initAfterBinding() {
        super.initAfterBinding()

        // 프로필 편집
        binding.tvEditProfile.setOnClickListener {
            navController.navigate(R.id.action_myPageFragment_to_editProfileFragment)
            dialog?.dismiss()
        }

        // 비밀번호 변경
        binding.tvChangePw.setOnClickListener {
            navController.navigate(R.id.action_myPageFragment_to_changePasswordFragment)
            dialog?.dismiss()
        }

        // 로그아웃
        binding.tvLogout.setOnClickListener {
            dialog?.dismiss()
            auth.signOut()

            // 저장된 prefs 삭제
            prefs.removeAll()

            navController.navigate(R.id.action_myPageFragment_to_loginFragment)
        }

    }
}