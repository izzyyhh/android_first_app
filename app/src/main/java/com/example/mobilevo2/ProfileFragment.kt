package com.example.mobilevo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobilevo2.databinding.ProfileFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(){
    private val arguments: ProfileFragmentArgs by navArgs()
    private val dbPeople = Firebase.firestore.collection("people")
    private val currentPersonRef = dbPeople.document(Firebase.auth.currentUser?.uid.toString())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ProfileFragmentBinding.bind(view)



        val personUId = if(arguments.personUid == "default") Firebase.auth.currentUser?.uid.toString() else arguments.personUid
        val viewedPersonRef = dbPeople.document(personUId)
        val adapter = ProfilePostsListAdapter()

        viewedPersonRef.get().addOnSuccessListener {
            val person = it.toObject<Person>() as Person
            val posts = person.posts

            adapter.submitList(posts)
        }

        binding.postsProfileList.layoutManager = GridLayoutManager(context, 3)
        binding.postsProfileList.adapter = adapter

        //edit button or follow button
        if(personUId != Firebase.auth.currentUser?.uid.toString()){
            binding.editButton.visibility = View.GONE
            var followed = false

            //unfollow or follow?
            viewedPersonRef.get().addOnSuccessListener {
                val person = it.toObject<Person>() as Person


                if(person.followers.contains(currentPersonRef)){
                    followed = true
                    binding.followButton.text = "UNFOLLOW"
                } else {
                    binding.followButton.text = "FOLLOW"
                }
            }

            binding.followButton.setOnClickListener {
                if(followed){
                    //unfollow
                    viewedPersonRef.update("followers", FieldValue.arrayRemove(currentPersonRef))
                            .addOnSuccessListener {
                                binding.followButton.text = "FOLLOW"
                                Snackbar.make(view, "unfollowed", Snackbar.LENGTH_SHORT).show()
                                followed = false

                                currentPersonRef.update("following", FieldValue.arrayRemove(viewedPersonRef))
                            }

                } else {
                    //follow
                    viewedPersonRef.update("followers", FieldValue.arrayUnion(currentPersonRef))
                            .addOnSuccessListener {
                                binding.followButton.text = "UNFOLLOW"
                                Snackbar.make(view, "followed", Snackbar.LENGTH_SHORT).show()
                                followed = true

                                currentPersonRef.update("following", FieldValue.arrayUnion(viewedPersonRef))
                            }
                }
            }

        } else {
            //show edit button instead, that means you cannot follow yourself and you cannot edit someone's profile
            binding.followButton.visibility = View.GONE
            binding.editButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editFragment)
            }
        }


        dbPeople.document(personUId)
                .get()
                .addOnSuccessListener {
                    val person = it.toObject<Person>()
                    binding.profileName.text = person?.fullName
                    binding.postsNum.text = person?.posts?.size.toString()
                    binding.followersNum.text = person?.followers?.size.toString()
                    binding.followingNum.text = person?.following?.size.toString()

                    //description
                    if ( person?.description == "" || person?.description == "null" || person?.description == null ){
                        binding.description.visibility = View.GONE
                    } else {
                        binding.description.text = person.description
                    }

                    //profile pic
                    if( person?.profilePicture == "" || person?.profilePicture == "null" || person?.profilePicture == null ){
                        binding.profilePicture.load(R.mipmap.ic_launcher) {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }
                    } else {
                        binding.profilePicture.load(person.profilePicture) {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }
                    }
                }



    }

}