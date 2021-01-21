package com.example.mobilevo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.mobilevo2.databinding.InnerNavHostFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InnerNavigationFragment : Fragment() {
    private lateinit var binding: InnerNavHostFragmentBinding
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //if user logs in, save him
        saveUser()
        return inflater.inflate(R.layout.inner_nav_host_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = InnerNavHostFragmentBinding.bind(view)
        navController = requireActivity().findNavController(R.id.innerNavHostFragment)
        //update actionbar

        NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        //bottom nav
        val bottomNav = binding.bottomNavigation
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.profileFragment -> {
                    //val action = ExploreFragmentDirections.actionExploreFragmentToProfileFragment(Firebase.auth.currentUser?.uid.toString())
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.exploreFragment -> {
                    navController.navigate(R.id.exploreFragment)
                    true
                }
                else -> {
                    FirebaseAuth.getInstance().signOut()
                    activity?.finish() //in order to delete history of navigation
                    navController.navigate(R.id.mainActivity)
                    true
                }
            }
        }

        bottomNav.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.exploreFragment -> {
                    navController.navigate(R.id.exploreFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun saveUser(){
        if(Firebase.auth.currentUser != null){
            val uid = Firebase.auth.currentUser?.uid
            val dbPeople = Firebase.firestore.collection("people")
            val docRefForThisUser = dbPeople.document(uid.toString())

            docRefForThisUser.get()
                    .addOnSuccessListener {
                        if (!it.exists()) {
                            //user is not saved in database yet
                            val person : Person = Person(
                                    Firebase.auth.currentUser?.displayName.toString()
                            )

                            dbPeople.document(uid.toString()).set(person)
                            Snackbar.make(requireView(), "welcome " + Firebase.auth.currentUser?.displayName, Snackbar.LENGTH_SHORT).show()

                        }
                    }
        }
    }
}