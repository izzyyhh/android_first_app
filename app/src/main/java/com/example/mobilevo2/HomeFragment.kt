package com.example.mobilevo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mobilevo2.databinding.ExampleFragmentBinding
import com.example.mobilevo2.databinding.HomeFragmentBinding
import com.example.mobilevo2.databinding.IzzyFragmentBinding

class HomeFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = HomeFragmentBinding.bind(view)

        binding.profileButton.setOnClickListener {
            val name = "Izzy Alastaii"
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment(name)
            view.findNavController().navigate(action)
        }
    }
}
