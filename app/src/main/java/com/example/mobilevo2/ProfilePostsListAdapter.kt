package com.example.mobilevo2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mobilevo2.databinding.PostProfileForListBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject

class ProfilePostsListAdapter : ListAdapter<DocumentReference, ProfilePostsListAdapter.ProfilePostsViewHolder>(DIFF_UTIL){

    class ProfilePostsViewHolder(private val binding: PostProfileForListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindPost(postRef : DocumentReference){
            postRef.get().addOnSuccessListener {
                val post = it.toObject<Post>()
                binding.postImage.load(post?.imageUrl){
                    crossfade(true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostProfileForListBinding.inflate(inflater, parent, false)

        return ProfilePostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfilePostsViewHolder, position: Int) {
        holder.bindPost(getItem(position))
    }

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<DocumentReference>(){
            override fun areItemsTheSame(oldItem: DocumentReference, newItem: DocumentReference): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: DocumentReference, newItem: DocumentReference): Boolean = oldItem == newItem
        }
    }
}

