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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis

@SuppressLint("CheckResult")// de asta am uitat sa ma ocup, cum am fost prins cu bugurile cu baza de date si log-in/register
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginFragment"

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //Auth
        auth = FirebaseAuth.getInstance()
        //Username Validation - need explanations
        //I guess RxTextView converts our text view to an observalbe
        val usernameStream = RxTextView.textChanges(binding.email)
            .skipInitialValue()
            .map { username -> username.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Email/Username")
        }
        //Password Validation - need explanations
        //I guess RxTextView converts our text view to an observalbe
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
            loginUser(email, password)
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

    /* Am creeat functia de login care ruleaza codul in thread-ul IO, atat timp cat frgamentul este alive
     * si are un timeout maxm de 500 ms dupa care ar trebuii sa il omoare daca ceva nu merge
     * daca log-in ul este realizat cu succes va merge catre firstFragment
     * */

    /* Cand rulez programul pe emulator si vreau sa accesez orice are legatura cu Firebase asta e warningul:
    * Ignoring header X-Firebase-Locale because its value was null.
    * fara nici o eraore, si de pe telefon merge perfect
    * */
    private fun loginUser(email: String, password: String) = lifecycleScope.launch(Dispatchers.IO) {
            withTimeout(500L) {
                if (isActive) {
                    val time = measureTimeMillis {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { login ->
                            if (login.isSuccessful) {
                                findNavController().navigate(R.id.action_loginFragment_to_FirstFragment)
                                Toast.makeText(context, "Login Successfull!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, login.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    Log.d(TAG,"Time to run code: $time ms.")
                }
            }
    }
}
