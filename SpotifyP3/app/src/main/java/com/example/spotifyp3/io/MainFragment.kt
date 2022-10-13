package com.example.spotifyp3.io

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spotifyp3.R
import com.example.spotifyp3.databinding.FragmentMainBinding
import com.example.spotifyp3.ui.HomeFragment
import com.example.spotifyp3.ui.LibraryFragment
import com.example.spotifyp3.ui.SearchFragment


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        replaceFragment(HomeFragment())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId){
                R.id.navBar_home -> replaceFragment(HomeFragment())
                R.id.navBar_search -> replaceFragment(SearchFragment())
                R.id.navBar_library -> replaceFragment(LibraryFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

}