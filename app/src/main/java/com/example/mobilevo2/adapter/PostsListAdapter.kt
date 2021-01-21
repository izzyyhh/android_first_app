package com.example.mobilevo2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobilevo2.ExploreFragmentDirections
import com.example.mobilevo2.R
import com.example.mobilevo2.data.Comment
import com.example.mobilevo2.data.Post
import com.example.mobilevo2.databinding.PostCardBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class PostsListAdapter : ListAdapter<Post, PostsListAdapter.MyViewHolder>(DIFF_UTIL) {

    class MyViewHolder(private val binding: PostCardBinding) : RecyclerView.ViewHolder(binding.root){
        private val currentPersonRef = Firebase.firestore.collection("people").document(Firebase.auth.currentUser?.uid.toString())

        fun bindMe(post: Post){
            //loading data into view
            //profile picture
            if(post.author.profilePicture != "" && post.author.profilePicture != null && post.author.profilePicture != "null"){
                binding.profileImage.load(post.author.profilePicture) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            } else {
                binding.profileImage.load(R.mipmap.ic_launcher) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
            binding.postedImage.load(post.imageUrl) {
                crossfade(true)
            }

            binding.myname.text = post.author.fullName
            binding.likes.text = post.likes.size.toString()
            binding.postDescription.text = post.text

            //description
            if(post.text == "" || post.text == null){
                binding.postDescription.visibility = View.GONE
            }

            //comments in own collection
            val postRef = Firebase.firestore.collection("posts").document(post.documentId)
            //how many likes
            postRef.collection("comments").addSnapshotListener { value, _ ->
                val comments = value?.toObjects<Comment>()
                binding.comments.text = comments?.size.toString()
            }

            //like button
            //is liked or is not liked, default is not liked
            if (post.likes.contains(currentPersonRef)){
                binding.favoriteButton.load(R.drawable.ic_baseline_favorite_24)
            }

            //user interaction
            //add snaplistener for updating view after updating like via like button
            postRef.addSnapshotListener { value, _ ->
                val post = value?.toObject<Post>() as Post
                binding.likes.text = post.likes.size.toString()

                if(post.likes.contains(currentPersonRef)){
                    binding.favoriteButton.load(R.drawable.ic_baseline_favorite_24)

                } else{
                    binding.favoriteButton.load(R.drawable.ic_baseline_favorite_border_24)
                }
            }

            //onclick, handle toggling like
            binding.favoriteButton.setOnClickListener {
                postRef.get().addOnSuccessListener {

                    val post = it?.toObject<Post>() as Post

                    if(post.likes.contains(currentPersonRef)){
                        //dont like anymore
                        postRef.update("likes", FieldValue.arrayRemove(currentPersonRef))

                    } else{
                        postRef.update("likes", FieldValue.arrayUnion(currentPersonRef))
                    }
                }

            }

            binding.commentButton.setOnClickListener{
                it.findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToCommentFragment(post.documentId))
            }



            //items which navigate to profile fragment
            val onclickToProfileItems = listOf(binding.myname, binding.profileImage)

            onclickToProfileItems.forEach{
                it.setOnClickListener { view ->
                    val action = ExploreFragmentDirections.actionExploreFragmentToProfileFragment(post.author.uid)
                    view.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostCardBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindMe(getItem(position))
    }

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Post>(){
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
        }
    }
}