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

class ProfileFragment : Fragment(){
    private val arguments: ProfileFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ProfileFragmentBinding.bind(view)

        val name = arguments.name;
        binding.profileName.text = name;

    }

}