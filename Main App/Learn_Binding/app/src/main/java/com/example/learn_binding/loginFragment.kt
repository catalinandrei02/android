package com.example.learn_binding

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

@SuppressLint("CheckResult")
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginFragment"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //Auth
        auth = FirebaseAuth.getInstance()
        //Username Validation - need explanations
        val usernameStream = RxTextView.textChanges(binding.email)
            .skipInitialValue()
            .map { username -> username.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Email/Username")
        }
        //Password Validation - need explanations
        val passwordStream = RxTextView.textChanges(binding.acPassword)
            .skipInitialValue()
            .map { password -> password.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Password")
        }
        //Button Enable
        val invalidFieldStream = Observable.combineLatest(
            usernameStream,
            passwordStream
        ) { usernameInvalid: Boolean, passwordInvalid: Boolean -> !usernameInvalid && !passwordInvalid }
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, R.color.primary_color) }
            } else {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, android.R.color.darker_gray) }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.acPassword.text.toString().trim()
            lifecycleScope.launch(Dispatchers.IO) {
                val time = measureTimeMillis {
                    loginUser(email, password)
                }
                Log.d(TAG,"Running time: $time ms.")
            }
        }
        binding.noAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.forgotPw.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }
    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Email/Username")
            binding.email.error = if (isNotValid) "$text can't be empty!" else null
        else if (text == "Password")
            binding.acPassword.error = if (isNotValid) "$text can't be empty!" else null
    }
    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { login ->
                if (login.isSuccessful) {
                    findNavController().navigate(R.id.action_loginFragment_to_FirstFragment)
                    Toast.makeText(context,"Login Successfull!",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,login.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }
}
