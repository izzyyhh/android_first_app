package com.example.mobilevo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mobilevo2.databinding.ExampleFragmentBinding
import com.example.mobilevo2.databinding.IzzyFragmentBinding
import com.example.mobilevo2.databinding.ProfileFragmentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(){
    private val arguments: ProfileFragmentArgs by navArgs()
    private val dbPeople = Firebase.firestore.collection("people")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ProfileFragmentBinding.bind(view)

        dbPeople.document(arguments.personUid)
                .get()
                .addOnSuccessListener {
                    val person = it.toObject<Person>()
                    binding.profileName.text = person?.fullName
                    binding.postsNum.text = person?.posts?.size.toString()
                    binding.followersNum.text = person?.followers?.size.toString()
                    binding.followingNum.text = person?.following?.size.toString()
                    binding.description.text = person?.description
                    //binding.profilePicture need default for profile picture
                }

    }

}