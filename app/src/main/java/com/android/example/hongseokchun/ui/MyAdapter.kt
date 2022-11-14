package com.android.example.hongseokchun.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class MyViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(private val context: Context, private val students: MutableList<Student>, private val posts: MutableList<Post>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val student = students[position]
        val post = posts[position]
//        holder.binding.textView1.text = student.id.toString()
//        holder.binding.textView2.text = student.name
//        holder.binding.root.setOnClickListener {
//            AlertDialog.Builder(context).setMessage("You clicked ${student.name}.").show()
//        }
//        holder.binding.buttonRemove.setOnClickListener {
//            students.removeAt(holder.adapterPosition)
//            notifyItemRemoved(holder.adapterPosition)
//        }
        holder.binding.detailviewitemProfileTextview.text = student.uid.toString()
        holder.binding.detailviewitemProfileImage.setImageResource(R.drawable.sample)
        holder.binding.detailviewitemCommentImageview.setImageResource(R.drawable.sample)
        holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.sample)
        holder.binding.detailviewitemExplainTextview.text = post.comment
        holder.binding.detailviewitemFavoritecounterTextview.text = post.like;
        holder.binding.detailviewitemImageviewContent.setImageResource(R.drawable.sample)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
