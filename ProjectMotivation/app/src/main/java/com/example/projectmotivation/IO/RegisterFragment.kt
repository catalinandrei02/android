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
import com.example.projectmotivation.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
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

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val compositeDisposable =  CompositeDisposable()
    private val TAG = "RegisterFragment"
    private val TAG_2 = "Cloud DataBase"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        checkStream()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            registerUser(email, password, name, username)
        }
        binding.tvRegisterBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
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

    private fun background(){
        val frameAnimation: AnimationDrawable = binding.animation.background as AnimationDrawable
        frameAnimation.setEnterFadeDuration(2500)
        frameAnimation.setExitFadeDuration(5000)
        frameAnimation.start()
    }

    private fun showNameExistAlert(isNotValid: Boolean){
        binding.etName.error = if (isNotValid) "Name can't be empty!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Username")
            binding.etUsername.error = if (isNotValid) "$text must contain at least 6 characters!" else null
        else if (text == "Password")
            binding.etPassword.error = if (isNotValid) "$text must contain at least 8 characters!" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.etEmail.error = if (isNotValid) "The e-mail you entered is not valid!" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.etPasswordCheck.error = if (isNotValid) "Password doesn't match!" else null
    }

    private fun registerUser(email: String, password: String, name:String, username:String) = lifecycleScope.launch(Dispatchers.IO) {
        withTimeout(500L) {
            if (isActive) {
                val time = measureTimeMillis {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                Toast.makeText(
                                    context,
                                    "You are now registered!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    val dataBaseRef = Firebase.firestore
                    val user: MutableMap<String,Any> = HashMap()
                    user["name"] = name
                    user["username"] = username
                    dataBaseRef.collection("users")
                        .add(user)
                        .addOnSuccessListener {
                            Log.w(TAG_2,"Added to database!")
                        }
                        .addOnFailureListener {
                            Log.w(TAG_2,"Failed to add to database!")
                        }
                }
                Log.d(TAG,"Time to run register: $time ms.")
            }
        }
    }

    private fun checkStream(){

        val nameStream = binding.etName.textChanges()
            .skipInitialValue()
            .map { name -> name.isEmpty()
            }
        nameStream.subscribe {
            showNameExistAlert(it)
        }
            .addTo(compositeDisposable)

        val emailStream = binding.etEmail.textChanges()
            .skipInitialValue()
            .map { email -> !Patterns.EMAIL_ADDRESS.matcher(email).matches() //What is this?
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }
            .addTo(compositeDisposable)

        val usernameStream = binding.etUsername.textChanges()
            .skipInitialValue()
            .map { username -> username.length < 6
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Username")
        }
            .addTo(compositeDisposable)

        val passwordStream = binding.etPassword.textChanges()
            .skipInitialValue()
            .map { password -> password.length < 8
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it,"Password")
        }
            .addTo(compositeDisposable)

        val passwordConfirmStream = Observable.merge(
           binding.etPassword.textChanges()
                .skipInitialValue()
                .map { password -> password.toString() != binding.etPasswordCheck.text.toString()
                },
            binding.etPasswordCheck.textChanges()
                .skipInitialValue()
                .map { confirmPassword -> confirmPassword.toString() != binding.etPassword.text.toString()
                })
        passwordConfirmStream.subscribe {
            showPasswordConfirmAlert(it)
        }
            .addTo(compositeDisposable)

        val invalidFieldStream = Observable.combineLatest(
            nameStream,
            emailStream,
            usernameStream,
            passwordStream,
            passwordConfirmStream
        ) { nameInvalid: Boolean, emailInvalid: Boolean, usernameInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
            !nameInvalid && !emailInvalid && !usernameInvalid && !passwordInvalid && !passwordConfirmInvalid }
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, R.color.light_goldenrod_yellow) }
            } else {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, R.color.bone) }
            }
        }
            .addTo(compositeDisposable)
    }
}