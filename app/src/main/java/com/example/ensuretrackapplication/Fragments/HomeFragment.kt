package com.example.ensuretrackapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ensuretrackapplication.Adapter
import com.example.ensuretrackapplication.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
     lateinit var binding : FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the la yout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.viewpager.adapter = Adapter(requireActivity().supportFragmentManager, lifecycle)
        TabLayoutMediator(binding.tab, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Client"
                }

                1 -> {
                    tab.text = "Switch"
                }

                2 -> {
                    tab.text = "SAVED"
                }
            }
        }.attach()
        return binding.root
    }
}