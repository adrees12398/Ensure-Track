package com.example.ensuretrackapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ensuretrackapplication.Fragments.ClientsFragment
import com.example.ensuretrackapplication.Fragments.SearchFragment

class Adapter(fragmentManager: FragmentManager,lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                ClientsFragment()
            }
            1->{
                SearchFragment()
            }
            else -> {
                Fragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}