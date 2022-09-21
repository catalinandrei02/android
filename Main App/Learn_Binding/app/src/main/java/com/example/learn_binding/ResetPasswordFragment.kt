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
import com.example.learn_binding.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)

        //Auth
        auth = FirebaseAuth.getInstance()
        //Email Validation - need explanations
        val emailStream = RxTextView.textChanges(binding.email)
            .skipInitialValue()
            .map { email -> !Patterns.EMAIL_ADDRESS.matcher(email).matches() //What is this?
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Reset Password
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                reset -> if (reset.isSuccessful) {
                    findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                    Toast.makeText(context,"Password reset email sent!",Toast.LENGTH_SHORT).show()
            } else {
                    Toast.makeText(context,reset.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }

        }
        binding.noAccount.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showEmailValidAlert(isNotValid: Boolean){
        if (isNotValid){
            binding.email.error = "The e-mail you entered is not valid!"
            binding.btnLogin.isEnabled = false
            binding.btnLogin.backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, android.R.color.darker_gray) }
        } else {
            binding.email.error = null
            binding.btnLogin.isEnabled = true
            binding.btnLogin.backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.primary_color) }
        }

    }

}
