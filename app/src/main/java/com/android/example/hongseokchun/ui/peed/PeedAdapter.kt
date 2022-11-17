package com.android.example.hongseokchun.ui

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.hongseokchun.R
import com.android.example.hongseokchun.databinding.PeedPostItemBinding
import com.android.example.hongseokchun.model.PostContent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MyViewHolder(val binding: PeedPostItemBinding) : RecyclerView.ViewHolder(binding.root)

class PeedAdapter(itemList: ArrayList<PostContent>) : RecyclerView.Adapter<MyViewHolder>() {
    lateinit var context: Context
    var itemList: ArrayList<PostContent> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.d("post itemlist",itemList.toString())
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PeedPostItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cpItemList = itemList.reversed() //최신 게시물이 위로

        val mainText = cpItemList[position].mainText
        val like = cpItemList[position].like
        val userName = "hong@hong.hong"
//        val imgName = cpItemList[position].imageNames[1]
//        loadImage(holder.binding.detailviewitemImageviewContent,imgName)
        loadImage(holder.binding.detailviewitemImageviewContent, itemList[position].imageNames[0])


       // val postid = itemList[position].
//        holder.binding.textView1.text = student.id.toString()
//        holder.binding.textView2.text = student.name
//        holder.binding.root.setOnClickListener {
//            AlertDialog.Builder(context).setMessage("You clicked ${student.name}.").show()
//        }
//        holder.binding.buttonRemove.setOnClickListener {
//            students.removeAt(holder.adapterPosition)
//            notifyItemRemoved(holder.adapterPosition)
//        }

        holder.binding.detailviewitemProfileTextview.text = userName
//        holder.binding.detailviewitemProfileImage.setImageResource(R.drawable.sample)
//        holder.binding.detailviewitemCommentImageview.setImageResource(R.drawable.sample)
//        holder.binding.detailviewitemFavoriteImageview.setImageResource(R.drawable.sample)
        holder.binding.detailviewitemExplainTextview.text = mainText
        holder.binding.detailviewitemFavoritecounterTextview.text = like;
//        holder.binding.detailviewitemImageviewContent.setImageResource(R.drawable.sample)
    }

    fun loadImage(imageView: ImageView, fileName: String){
        val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://hongseokchun-1f848.appspot.com")
        val storageRef: StorageReference = storage.reference
        storageRef.child("postImage/hong@hong.hong/post_img1.jpg").downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView)
            }
        }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)

            }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
