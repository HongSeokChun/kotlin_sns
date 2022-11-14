package com.android.example.hongseokchun.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.databinding.ItemLayoutBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ImageAdapter(itemList: ArrayList<Uri>)
    : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    lateinit var context: Context

    var itemList: ArrayList<Uri> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("friend itemlist", itemList.toString())
        }

    inner class ViewHolder(itemViewBinding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        val imageView: ImageView = itemViewBinding.imageSlider
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        holder.imageView.setImageURI(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

}