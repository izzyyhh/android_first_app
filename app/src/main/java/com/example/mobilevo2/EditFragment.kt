package com.example.mobilevo2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobilevo2.databinding.EditFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.InputStream

class EditFragment : Fragment() {
    private lateinit var binding: EditFragmentBinding
    private val currentPersonRef = Firebase.firestore.collection("people").document(Firebase.auth.currentUser?.uid.toString())
    private lateinit var currentPerson: Person
    private var uniqueImageRef: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EditFragmentBinding.bind(view)

        currentPersonRef.get()
                .addOnSuccessListener {
                    currentPerson = it.toObject<Person>() as Person
                    binding.inputName.setText(currentPerson.fullName)

                    if (currentPerson.description != "" && currentPerson.description != null && currentPerson.description != "null") {
                        binding.inputDescription.setText(currentPerson.description)
                    }

                    if (currentPerson.profilePicture != "" && currentPerson.profilePicture != null && currentPerson.profilePicture != "null") {
                        binding.fromGalleryImg.visibility == View.GONE
                        binding.profilePicture.load(currentPerson.profilePicture) {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }
                    }

                }


        binding.profilePicture.setOnClickListener {
            getImageFromGallery()
        }

        binding.updateButton.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        if (uniqueImageRef != "") {
            Firebase.storage.reference.child("profile-pictures/${uniqueImageRef}").downloadUrl
                    .addOnSuccessListener { imageUrl ->
                        currentPersonRef.update("profile_picture", imageUrl.toString())
                    }
        }

        currentPersonRef.update(mapOf(
                "full_name" to binding.inputName.text.toString(),
                "description" to binding.inputDescription.text.toString()
        )).addOnSuccessListener {
            Snackbar.make(requireView(), "updated succesfully", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToProfileFragment())
        }.addOnFailureListener {
            Snackbar.make(requireView(), "update failed", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getImageFromGallery() {
        val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        startActivityForResult(pickIntent, EditFragment.PICK_IMAGE_REQUEST_CODE)
    }

    private fun storeProfileImage(imageUri: Uri) {
        uniqueImageRef = "${Timestamp.now().nanoseconds}_${Timestamp.now().seconds}_${imageUri.lastPathSegment}"
        val storageRef = Firebase.storage.reference.child("profile-pictures/${uniqueImageRef}")
        val imageStream = context?.contentResolver?.openInputStream(imageUri) as InputStream

        storageRef.putStream(imageStream)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            val imageUri = data.data as Uri
            binding.fromGalleryImg.visibility = View.GONE
            binding.profilePicture.load(imageUri) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }

            storeProfileImage(imageUri)
        }
    }

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 2021
    }
}