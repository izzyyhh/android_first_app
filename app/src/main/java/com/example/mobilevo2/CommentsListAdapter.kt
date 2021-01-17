package com.example.mobilevo2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobilevo2.databinding.CommentListItemBinding

class CommentsListAdapter : ListAdapter<Comment, CommentsListAdapter.CommentsViewHolder>(DIFF_UTIL) {

    class CommentsViewHolder(private val binding: CommentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindComment(comment: Comment){
            if(comment.author.profilePicture != null && comment.author.profilePicture != "" && comment.author.profilePicture != "null"){
                binding.profilePicture.load(comment.author.profilePicture){
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
            binding.authorName.text = comment.author.fullName
            binding.text.text = comment.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentListItemBinding.inflate(inflater, parent, false)

        return CommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        return holder.bindComment(getItem(position))
    }

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean = oldItem == newItem
        }
    }
}