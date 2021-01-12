package com.example.mobilevo2


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobilevo2.databinding.ForAdapterMyBinding

class MyAdapter : ListAdapter<PostFirebase, MyAdapter.MyViewHolder>(DIFF_UTIL) {

    class MyViewHolder(private val binding: ForAdapterMyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindMe(post: PostFirebase){
            binding.profileImage.load(post.author.profilePicture) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            binding.postedImage.load(post.imageUrl) {
                crossfade(true)
            }

            binding.myname.text = post.author.fullName
            binding.likes.text = post.likes.size.toString()
            binding.comments.text = 12.toString()

            val onclickToProfileItems = listOf(binding.myname, binding.profileImage)

            onclickToProfileItems.forEach{
                it.setOnClickListener { view ->
                    val action = ExploreFragmentDirections.actionExploreFragmentToProfileFragment(post.author.fullName)
                    view.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForAdapterMyBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindMe(getItem(position))
    }

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<PostFirebase>(){
            override fun areItemsTheSame(oldItem: PostFirebase, newItem: PostFirebase): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: PostFirebase, newItem: PostFirebase): Boolean = oldItem == newItem
        }
    }
}