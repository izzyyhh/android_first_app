package com.example.mobilevo2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.Scopes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthenticationFragment : Fragment(R.layout.authentication_fragment) {

    private val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().setScopes(listOf(Scopes.PROFILE)).build()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Firebase.auth.currentUser == null){
            login()
        } else {
            navigateToHome()
        }
    }

    private fun login(){
        val authIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()

        startActivityForResult(authIntent, REQUEST_CODE_LOG_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_LOG_IN){
            val response = IdpResponse.fromResultIntent(data)

            if(response == null){
                Snackbar.make(requireView(), "log in failed", Snackbar.LENGTH_SHORT).show()
                return
            }

            if(response.error == null){

                Snackbar.make(requireView(), "succesfully logged in", Snackbar.LENGTH_SHORT).show()
                navigateToHome()
            }
        }
    }

    private fun navigateToHome(){
        findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToInnerNavigationFragment())
    }

    companion object {
        const val REQUEST_CODE_LOG_IN = 1999
    }
}