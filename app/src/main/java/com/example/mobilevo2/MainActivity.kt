package com.example.mobilevo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mobilevo2.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        /*val binding: ActivityMainBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main)*/
        //oldschool mit setConentvie(r.layout.activity.main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //jawoi gschoff mid view binding, voi geil sisd miassds R.iwos mochn
        //google use viewbindin android erstellt für edes xml eine eigene klasse
        //developer.android OP
        //Oldschool: val mynameView: TextView = findViewById(R.id.profileName)
        //mit viewbindin:

        /* auskommentiert weil i fragments einbauen woid
        binding.profileName.setOnClickListener{
            binding.profileName.text = "ohaaaaaaaaa viewbinding lul no viewFindById anymore :DD"
        }*/
    }
}