package com.example.spotifyp3.io

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.spotifyp3.R
import com.example.spotifyp3.databinding.FragmentWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
            }
            btnGoogle.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
            }
            btnFacebook.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
            }
            tvLogin.setOnClickListener {
                    findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_welcomeFragment_to_mainFragment)
        }
    }
}