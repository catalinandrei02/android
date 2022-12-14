package com.example.projectmotivation.io

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectmotivation.R
import com.example.projectmotivation.databinding.FragmentResetBinding
import com.example.projectmotivation.utils.Constants
import com.example.projectmotivation.utils.Constants.TAG.RESET
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis

class ResetFragment : Fragment() {

    private lateinit var binding: FragmentResetBinding
    private lateinit var auth: FirebaseAuth
    private val compositeDisposable =  CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetBinding.inflate(inflater,container,false)
        auth = Firebase.auth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkStream()
        binding.btnReset.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            resetPassword(email)
        }
    }

    override fun onStart() {
        super.onStart()
        background()
    }

    private fun background(){
        val frameAnimation: AnimationDrawable = binding.animation.background as AnimationDrawable
        frameAnimation.apply {
            setEnterFadeDuration(2500)
            setExitFadeDuration(5000)
            start()
        }
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        if (isNotValid){
            binding.apply {
                etEmail.error = Constants.Error.EMAIL
                btnReset.isEnabled = false
                btnReset.backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, R.color.light_goldenrod_yellow) }
            }
        } else {
            binding.apply {
                etEmail.error = null
                btnReset.isEnabled = true
                btnReset.backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it,  R.color.bone) }
            }
        }
    }

    private fun resetPassword(email: String) = lifecycleScope.launch(Dispatchers.IO){
        withTimeout(500L){
            val time = measureTimeMillis {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                        reset -> if (reset.isSuccessful) {
                    findNavController().navigate(R.id.action_resetFragment_to_loginFragment)
                    Toast.makeText(context,"Password reset email sent!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,reset.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Log.d(RESET,"Time to run code: $time ms.")
        }
    }

    private fun checkStream(){
        val emailStream = binding.etEmail.textChanges()
            .skipInitialValue()
            .map { email -> !Patterns.EMAIL_ADDRESS.matcher(email).matches() //What is this?
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }.addTo(compositeDisposable)
    }
}