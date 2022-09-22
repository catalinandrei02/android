package com.example.learn_binding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentFourthBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*


class FragmentFourth : Fragment() {

    private lateinit var binding: FragmentFourthBinding
    //Database initialization
    private val resourceCollectionRef = Firebase.firestore.collection("resurse")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFourthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.add.setOnClickListener {
                val water = binding.apa.text.toString().toInt()
                val milk = binding.lapte.text.toString().toInt()
                val beans = binding.boabe.text.toString().toInt()
                val cups = binding.pahare.text.toString().toInt()
                val money = binding.bani.text.toString().toInt()
            val resurse = Resurse(water, milk, beans, cups, money)
            saveResources(resurse)
        }
    }
    private fun saveResources(resurse: Resurse) = CoroutineScope(Dispatchers.IO).launch {
        try {
            resourceCollectionRef.add(resurse)
            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.action_fragmentFourth_to_FirstFragment)
                Toast.makeText(context,"Successfully added new resources!",Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }

}