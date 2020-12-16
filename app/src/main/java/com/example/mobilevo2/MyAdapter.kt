package com.example.mobilevo2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilevo2.databinding.ForAdapterMyBinding

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    data class Post(
        val userId: Int,
        val userName: String
    )


    private val fakeData = listOf(
        Post(
            userId = 1,
            userName = "izzy"
        ),
        Post(userId = 2,
            userName = "genti"
        ),
        Post(
            userId = 3,
            userName = "jimmy"
        )
    )

    class MyViewHolder(private val binding: ForAdapterMyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindMe(post: Post){
            binding.myname.text = post.userId.toString() + " " + post.userName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForAdapterMyBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post: Post = fakeData[position]
        holder.bindMe(post)
    }

    override fun getItemCount(): Int {
        return fakeData.size
    }
}