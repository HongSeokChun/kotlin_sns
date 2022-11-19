package com.android.example.hongseokchun.ui.info

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import com.android.example.hongseokchun.MainActivity
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.base.BaseFragment
import com.android.example.hongseokchun.databinding.FragmentMemberFindLoginIdBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserFindLoginIdFragment : BaseFragment<FragmentMemberFindLoginIdBinding>(R.layout.fragment_member_find_login_id) {
    private val db = Firebase.firestore

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setNavShow("아이디찾기")
    }

    override fun initDataBinding() {
        super.initDataBinding()

        binding.btnFindID2.setOnClickListener {
            val name = binding.tvName.text.toString()

            if(name.isEmpty()){
                Toast.makeText(context,"이름을 입력해주세요", Toast.LENGTH_LONG).show()
            }

            db.collection("users")
                .whereEqualTo("name", name)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() == 0){
                        AlertDialog.Builder(context).run {
                            setTitle("아이디 찾기")
                            setMessage("가입정보가 없습니다. 이름을 확인해주세요")
                            setPositiveButton("확인",null)
                            show()
                        }
                        binding.tvName.setText("")
                    }
                    for (document in documents) {
                        // if문으로 db 답찾고 화면 답이랑 맞는지 확인
                        AlertDialog.Builder(context).run {
                            setTitle("아이디 찾기")
                            setMessage("${name}님의 아이디는 ${document.id}입니다.")
                            setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    navController.navigate(R.id.action_userFindLoginIdFragment_to_loginFragment)
                                })
                            show()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }

    }


    override fun initAfterBinding() {
        super.initAfterBinding()
    }

}

