package com.example.spotifyp3.io

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
import com.example.spotifyp3.R
import com.example.spotifyp3.databinding.FragmentLoginBinding
import com.example.spotifyp3.utils.Constants
import com.example.spotifyp3.utils.Constants.TAG.LOGIN
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
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                loginUser(email, password)
            }
            btnLoginGoogle.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                loginUser(email, password)
            }
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }
    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Email")
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
                            } else {
                                Toast.makeText(context, login.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                Log.d(LOGIN,"Time to run log-in: $time ms.")
            }
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
                        context?.let { ContextCompat.getColorStateList(it, R.color.spotify_green) }
                }
            } else {
                binding.apply {
                    btnLogin.isEnabled = false
                    btnLogin.backgroundTintList =
                        context?.let { ContextCompat.getColorStateList(it, R.color.grey) }
                }
            }
        }.addTo(compositeDisposable)
    }
}

