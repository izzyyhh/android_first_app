package com.example.mobilevo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilevo2.databinding.CommentFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class CommentFragment : Fragment() {
    private lateinit var binding: CommentFragmentBinding
    private val arguments: CommentFragmentArgs by navArgs()
    private lateinit var dbPostRef: DocumentReference
    private lateinit var currentPersonRef: DocumentReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.comment_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CommentFragmentBinding.bind(view)

        dbPostRef = Firebase.firestore.collection("posts").document(arguments.postId)
        currentPersonRef = Firebase.firestore.collection("people").document(Firebase.auth.currentUser?.uid.toString())

        val adapter = CommentsListAdapter()
        dbPostRef.collection("comments").get()
                .addOnSuccessListener { commentsSnapShot ->
                    val comments = commentsSnapShot.toObjects<Comment>()
                    adapter.submitList(comments)
                }

        binding.commentList.layoutManager = LinearLayoutManager(context)
        binding.commentList.adapter = adapter


        binding.commentButton.setOnClickListener {
            if ( binding.commentInput.text.toString() == "" ){
                Snackbar.make(view, "write a comment first", Snackbar.LENGTH_SHORT).show()
            } else{
                setComment()
            }
        }

    }

    private fun setComment(){
        currentPersonRef.get().addOnSuccessListener { personSnapShot ->
            val person = personSnapShot.toObject<Person>()
            val author = Author(Firebase.auth.currentUser?.uid.toString(), person?.fullName.toString(), person?.profilePicture.toString())

            val newCommentRef = dbPostRef.collection("comments").document()
            val comment = Comment(newCommentRef.id, author, binding.commentInput.text.toString())
            newCommentRef.set(comment)
                    .addOnSuccessListener {
                        Snackbar.make(requireView(), "commented with: ${comment.text}", Snackbar.LENGTH_LONG).show()
                        findNavController().navigate(CommentFragmentDirections.actionCommentFragmentToExploreFragment())
                    }
        }

    }
}