package com.example.mobilevo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilevo2.databinding.ExampleFragmentBinding
import com.example.mobilevo2.databinding.ExploreFragmentBinding
import com.example.mobilevo2.databinding.IzzyFragmentBinding

class ExploreFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ExploreFragmentBinding.bind(view)

        val adapter = MyAdapter()

        binding.izzysList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.izzysList.adapter = adapter
    }
}
