package com.android.example.hongseokchun.ui.peed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.databinding.CommentItemBinding
import com.android.example.hongseokchun.model.AlarmDTO
import com.android.example.hongseokchun.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//data class Student(val uid: Int, val name: String)

class CommentViewHolder(val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)

class CommentAdapter(private val comments: MutableList<Comment>)
        : RecyclerView.Adapter<CommentViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CommentItemBinding.inflate(inflater, parent, false)
                return CommentViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
                val comment = comments[position]

//                holder.binding.commentviewitemTextviewComment.text = comment.message
//                holder.binding.commentviewitemTextviewProfile.text = comment.User.name;
//                holder.binding.commentviewitemImageviewProfile.setImageResource(R.drawable.sample);
        }

        override fun getItemCount(): Int {
                return comments.size
        }
        //글 달았을때 알람 기능
        fun commentAlarm(destinationUid : String, message : String){
                var alarmDTO = AlarmDTO()
                alarmDTO.destinationUid = destinationUid
                alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
                alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
                alarmDTO.timestamp = System.currentTimeMillis()
                alarmDTO.message = message
                FirebaseFirestore.getInstance().collection("alarm").document().set(alarmDTO)
        }
}
