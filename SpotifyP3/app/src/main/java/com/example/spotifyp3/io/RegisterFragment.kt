package com.example.spotifyp3.io

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spotifyp3.R
import com.example.spotifyp3.data.UserEntity
import com.example.spotifyp3.data.UserViewModel
import com.example.spotifyp3.databinding.FragmentRegisterBinding
import com.example.spotifyp3.utils.Constants
import com.example.spotifyp3.utils.Constants.TAG.REGISTER
import com.example.spotifyp3.utils.Constants.Url.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
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
    private lateinit var dbRef: DatabaseReference
    private lateinit var mUserViewModel: UserViewModel
    private val compositeDisposable =  CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkStream()

        binding.apply {
            btnBack.setOnClickListener{
                findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
            }
            btnCreateAccount.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val gender = etGender.text.toString()
                val name = etName.text.toString()
                registerUser(email, password, gender, name)
            }
        }
    }
    private fun showNameExistAlert(isNotValid: Boolean){
        binding.etName.error = if (isNotValid) Constants.Error.NAME else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Password")
            binding.etPassword.error = if (isNotValid) "$text " + Constants.Error.CHAR_8 else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.etEmail.error = if (isNotValid) Constants.Error.EMAIL else null
    }

    private fun registerUser(email: String, password: String, gender:String, name:String) = lifecycleScope.launch(
        Dispatchers.IO) {
        withTimeout(500L) {
            if (isActive) {
                val time = measureTimeMillis {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val userData = UserEntity(0,email,name, gender,"")
                                mUserViewModel.addUser(userData)
                                val user: FirebaseUser? = auth.currentUser
                                val userId:String = user!!.uid
                                dbRef = Firebase.database.reference.child(USER).child(userId)
                                val hashMap: HashMap<String,String> = HashMap()
                                hashMap["userId"] = userId
                                hashMap["userName"] = name
                                hashMap["userGender"] = gender
                                hashMap["userImage"] = ""
                                dbRef.setValue(hashMap).addOnCompleteListener {
                                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                }
                            } else {
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                }
                Log.d(REGISTER,"Time to run register: $time ms.")
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
        }.addTo(compositeDisposable)

        val emailStream = binding.etEmail.textChanges()
            .skipInitialValue()
            .map { email -> !Patterns.EMAIL_ADDRESS.matcher(email).matches() //What is this?
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }.addTo(compositeDisposable)


        val passwordStream = binding.etPassword.textChanges()
            .skipInitialValue()
            .map { password -> password.length < 8
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it,"Password")
        }.addTo(compositeDisposable)


        val invalidFieldStream = Observable.combineLatest(
            nameStream,
            emailStream,
            passwordStream
        ) { nameInvalid: Boolean, emailInvalid: Boolean, passwordInvalid: Boolean ->
            !nameInvalid && !emailInvalid && !passwordInvalid}
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.apply {
                    btnCreateAccount.isEnabled = true
                    btnCreateAccount.backgroundTintList =
                        context?.let { ContextCompat.getColorStateList(it, R.color.spotify_green) }
                }
            } else {
                binding.apply {
                    btnCreateAccount.isEnabled = false
                    btnCreateAccount.backgroundTintList =
                        context?.let { ContextCompat.getColorStateList(it, R.color.grey) }
                }
            }
        }.addTo(compositeDisposable)
    }

}