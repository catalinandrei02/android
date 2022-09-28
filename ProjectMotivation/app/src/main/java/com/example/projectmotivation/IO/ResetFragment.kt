package com.example.projectmotivation.io

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectmotivation.R
import com.example.projectmotivation.databinding.FragmentResetBinding

class ResetFragment : Fragment() {

    private lateinit var binding: FragmentResetBinding
    private val TAG = "ResetFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        background()
    }

    private fun background(){
        val frameAnimation: AnimationDrawable = binding.animation.background as AnimationDrawable
        frameAnimation.setEnterFadeDuration(2500)
        frameAnimation.setExitFadeDuration(5000)
        frameAnimation.start()
    }


}