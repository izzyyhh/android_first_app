package com.example.mobilevo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilevo2.databinding.ExampleFragmentBinding
import com.example.mobilevo2.databinding.ExploreFragmentBinding
import com.example.mobilevo2.databinding.IzzyFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ExploreFragment : Fragment(){
    private val db = Firebase.firestore.collection("posts")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ExploreFragmentBinding.bind(view)
        val adapter = MyAdapter()

        db.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener{ v, error ->
            val posts = v?.toObjects<Post>().orEmpty()
            adapter.submitList(posts)
        }

        binding.izzysList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.izzysList.adapter = adapter

        binding.postButton.setOnClickListener {
            findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToPostFragment())
        }

    }
}
