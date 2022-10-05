package com.example.projectmotivation.io

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectmotivation.R
import com.example.projectmotivation.databinding.FragmentLoginBinding
import com.example.projectmotivation.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val compositeDisposable =  CompositeDisposable()
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        auth = Firebase.auth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkStream()
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                loginUser(email, password)
            }

            tvLoginRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvLoginReset.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_resetFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        background()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Email/Username")
            binding.etEmail.error = if (isNotValid) "$text " + Constants.Error.EMPTY else null
        else if (text == "Password")
            binding.etPassword.error = if (isNotValid) "$text " + Constants.Error.EMPTY else null
    }

    private fun loginUser(email: String, password: String) = lifecycleScope.launch(Dispatchers.IO) {
        withTimeout(500L) {
            if (isActive) {
                val time = measureTimeMillis {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { login ->
                            if (login.isSuccessful) {
                                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                                Toast.makeText(context, "Login Successfully!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, login.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                Log.d(TAG,"Time to run log-in: $time ms.")
            }
        }
    }

    private fun background(){
        val frameAnimation: AnimationDrawable = binding.animation.background as AnimationDrawable
        frameAnimation.apply {
            setEnterFadeDuration(2500)
            setExitFadeDuration(5000)
            start()
        }
    }

    private fun checkStream() {
        val usernameStream = binding.etEmail.textChanges()
            .skipInitialValue()
            .map { username -> username.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Email/Username")
        }.addTo(compositeDisposable)

        val passwordStream = binding.etPassword.textChanges()
            .skipInitialValue()
            .map { password -> password.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Password")
        }.addTo(compositeDisposable)

        val invalidFieldStream = Observable.combineLatest(
            usernameStream,
            passwordStream
        ) { usernameInvalid: Boolean, passwordInvalid: Boolean -> !usernameInvalid && !passwordInvalid }
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.apply {
                    btnLogin.isEnabled = true
                    btnLogin.backgroundTintList =
                        context?.let { ContextCompat.getColorStateList(it, R.color.light_goldenrod_yellow) }
                }
            } else {
                binding.apply {
                    btnLogin.isEnabled = false
                    btnLogin.backgroundTintList =
                        context?.let { ContextCompat.getColorStateList(it, R.color.bone) }
                }
            }
        }.addTo(compositeDisposable)
    }
}