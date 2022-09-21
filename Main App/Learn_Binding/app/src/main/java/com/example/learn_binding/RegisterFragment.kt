package com.example.learn_binding

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable


@SuppressLint("CheckResult") //what is this?
class RegisterFragment : Fragment() {

    // binding
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        //AUTH
        auth = FirebaseAuth.getInstance()
    //Full Name Validation - need explanations
        val nameStream = RxTextView.textChanges(binding.fullnamee)
            .skipInitialValue()
            .map { name -> name.isEmpty()
            }
        nameStream.subscribe {
            showNameExistAlert(it)
        }
    //Email Validation - need explanations
        val emailStream = RxTextView.textChanges(binding.email)
            .skipInitialValue()
            .map { email -> !Patterns.EMAIL_ADDRESS.matcher(email).matches() //What is this?
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }
    //Username Validation - need explanations
        val usernameStream = RxTextView.textChanges(binding.usernamee)
            .skipInitialValue()
            .map { username -> username.length < 6
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Username")
        }
    //Password Validation - need explanations
        val passwordStream = RxTextView.textChanges(binding.acPassword)
            .skipInitialValue()
            .map { password -> password.length < 8
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Password")
        }
    //Confirm Password Validation - Observable?
        val passwordConfirmStream = Observable.merge(
            RxTextView.textChanges(binding.acPassword)
                .skipInitialValue()
                .map { password -> password.toString() != binding.acPasswordCheck.text.toString()
                },
            RxTextView.textChanges(binding.acPasswordCheck)
                .skipInitialValue()
                .map { confirmPassword -> confirmPassword.toString() != binding.acPassword.text.toString()
                })
            passwordConfirmStream.subscribe {
                showPasswordConfirmAlert(it)
            }
        //Button Enable
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
                binding.btnLogin.isEnabled = true
                binding.btnLogin.backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, R.color.primary_color) } //had an error and Android studio fixed but I don't quite get it
            } else {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.backgroundTintList =
                    context?.let { ContextCompat.getColorStateList(it, android.R.color.darker_gray) }
            }
        }
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //The buttons
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.acPassword.text.toString().trim()
            registerUser(email, password)
        }
        binding.yesAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Error Functions
    private fun showNameExistAlert(isNotValid: Boolean){
        binding.fullnamee.error = if (isNotValid) "Name can't be empty!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Username")
            binding.usernamee.error = if (isNotValid) "$text must contain at least 6 characters!" else null
        else if (text == "Password")
            binding.acPassword.error = if (isNotValid) "$text must contain at least 8 characters!" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.email.error = if (isNotValid) "The e-mail you entered is not valid!" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.acPasswordCheck.error = if (isNotValid) "Password doesn't match!" else null
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    Toast.makeText(context,"You are now registered!",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }
}