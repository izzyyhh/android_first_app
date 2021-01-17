package com.example.mobilevo2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.mobilevo2.databinding.PostFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.InputStream

class PostFragment : Fragment() {
    val storage = Firebase.storage
    val dbPosts = Firebase.firestore.collection("posts")
    val dbPeople = Firebase.firestore.collection("people")
    private lateinit var binding : PostFragmentBinding
    private lateinit var newPostRef : DocumentReference
    private lateinit var imageStream: InputStream
    private var uploadSuccessful = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = PostFragmentBinding.bind(view)

        binding.imageView.setOnClickListener {
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            pickIntent.type = "image/*"
            startActivityForResult(pickIntent, PICK_IMAGE_REQUEST_CODE)

        }

        binding.postButton.setOnClickListener {
            if(uploadSuccessful){
                //get author
                dbPeople.document(Firebase.auth.currentUser?.uid.toString())
                    .get()
                    .addOnSuccessListener { personResult ->
                        val person  = personResult.toObject<Person>()
                        val author = Author(
                            Firebase.auth.currentUser?.uid.toString(),
                            person?.fullName.toString(),
                            person?.profilePicture.toString()
                        )
                        //get image cdn url
                        val postImage = storage.reference.child("posts-images").child(newPostRef.id)

                        postImage.downloadUrl.addOnSuccessListener { url ->
                            val newPost : Post = Post(
                                newPostRef.id,
                                url.toString(),
                                author,
                                binding.postDescription.text.toString()
                            )

                            newPostRef.set(newPost)
                                .addOnSuccessListener {

                                    dbPeople.document(Firebase.auth.currentUser?.uid.toString()).update("posts", FieldValue.arrayUnion(newPostRef))
                                            .addOnSuccessListener {
                                                Snackbar.make(view, "post uploaded successfully", Snackbar.LENGTH_SHORT).show()
                                                findNavController().navigate(PostFragmentDirections.actionPostFragmentToExploreFragment())
                                            }
                                            .addOnFailureListener{
                                                Snackbar.make(view, "posted but something went wrong, when saving it to the user", Snackbar.LENGTH_SHORT).show()
                                            }
                                }
                                .addOnFailureListener{
                                    Snackbar.make(view, "post could not be uploaded, try again", Snackbar.LENGTH_SHORT).show()
                                }
                    }
                }

            } else {
                Snackbar.make(view, "select an image first, to post something", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){

            if(data == null || data.data == null){
                //return if nothing is selected
                return
            }

            //show selected image
            val imageUri = data.data as Uri
            binding.fromGalleryImg.visibility = View.GONE
            binding.imageView.load(imageUri)

            //get new id and saved it with it
            newPostRef = dbPosts.document()
            val newPostId = newPostRef.id
            imageStream = context?.contentResolver?.openInputStream(imageUri) as InputStream
            val imageRef = storage.reference.child("posts-images/${newPostId}")
            
            val uploadTask = imageRef.putStream(imageStream)
            uploadTask.addOnSuccessListener {
                uploadSuccessful = true
            }
        }
    }

    companion object{
        const val PICK_IMAGE_REQUEST_CODE = 2001
    }
}