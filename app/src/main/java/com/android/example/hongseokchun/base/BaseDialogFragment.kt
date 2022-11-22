package com.android.example.hongseokchun.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

abstract class BaseDialogFragment <B: ViewDataBinding> (@LayoutRes private  val layoutResourceId: Int) :
    DialogFragment() {

    // protected abstract val viewModel: VM
    protected lateinit var binding: B
    protected lateinit var navController: NavController

    // * 레이아웃을 띄운 직후 호출. * 뷰나 액티비티의 속성 등을 초기화. * ex) 리사이클러뷰, 툴바, 드로어뷰..
    protected open fun initStartView() {}
    // * 데이터 바인딩 설정.
    protected open fun initDataBinding() {}
    // * 바인딩 이후에 할 일을 여기에 구현. * 그 외에 설정할 것이 있으면 이곳에서 설정. * 클릭 리스너도 이곳에서 설정.
    protected open fun initAfterBinding() {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // false : 화면 밖 터치 혹은 뒤로가기 버튼 누를 시 dismiss 안됨
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 둥근 모서리 적용
        //dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE) //android version 4.4 이하에서 blue line 생기는거 방지
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initStartView()
        initDataBinding()
        initAfterBinding()
    }
}