package com.example.projectmotivation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectmotivation.R
import com.example.projectmotivation.content.*
import com.example.projectmotivation.databinding.FragmentMainBinding


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
                R.id.navBar_profile -> replaceFragment(ProfileFragment())
                R.id.navBar_chat -> replaceFragment(ChatFragment())
                R.id.navBar_settings -> replaceFragment(SettingsFragment())
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