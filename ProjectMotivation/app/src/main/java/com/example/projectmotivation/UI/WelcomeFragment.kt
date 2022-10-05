package com.example.projectmotivation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectmotivation.databinding.FragmentWelcomeBinding
import android.graphics.drawable.AnimationDrawable
import androidx.navigation.fragment.findNavController
import com.example.projectmotivation.R
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
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_welcomeFragment_to_mainFragment)
        }
        background()
    }
    private fun background(){
        val frameAnimation: AnimationDrawable = binding.animation.background as AnimationDrawable
        frameAnimation.setEnterFadeDuration(2500)
        frameAnimation.setExitFadeDuration(5000)
        frameAnimation.start()
    }
}