package com.example.mobilevo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilevo2.databinding.ExploreFragmentBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ExploreFragment : Fragment(){
    private val db = Firebase.firestore.collection("posts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ExploreFragmentBinding.bind(view)
        val adapter = PostsListAdapter()

        binding.postsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.postsList.adapter = adapter

        db.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener{ v, error ->
            val posts = v?.toObjects<Post>().orEmpty()
            adapter.submitList(posts)
        }



        binding.postButton.setOnClickListener {
            findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToPostFragment())
        }

    }
}
