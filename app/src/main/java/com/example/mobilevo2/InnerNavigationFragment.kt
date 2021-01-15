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
import com.example.mobilevo2.databinding.InnerNavFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InnerNavigationFragment : Fragment() {
    private lateinit var binding: InnerNavFragmentBinding
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.inner_nav_fragment, container, false)
       // bottomNav.setOnNavigationItemReselectedListener {
       //     false
        //}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = InnerNavFragmentBinding.bind(view)
        navController = requireActivity().findNavController(R.id.innerNavHostFragment)
        //update actionbar

        NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        //bottom nav
        val bottomNav = binding.bottomNavigation
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.profileFragment -> {
                    val action = ExploreFragmentDirections.actionExploreFragmentToProfileFragment(Firebase.auth.currentUser?.uid.toString())
                    navController.navigate(action)
                    true
                }
                R.id.exploreFragment -> {
                    navController.navigate(R.id.exploreFragment)
                    true
                }
                else -> {
                    FirebaseAuth.getInstance().signOut()

                    navController.navigate(R.id.mainActivity)
                    true
                }
            }
        }
    }




}