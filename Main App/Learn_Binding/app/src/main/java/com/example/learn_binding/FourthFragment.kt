package com.example.learn_binding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentFourthBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentFourth : Fragment() {

    private val viewModel: ResurseViewModel by activityViewModels()
    private lateinit var binding: FragmentFourthBinding
    //Database initialization

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFourthBinding.inflate(inflater, container, false)
        viewModel.getResources()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.add.setOnClickListener {
            saveResources()
        }
    }
    private fun saveResources() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val water = viewModel.db.onlWater + binding.apa.text.toString().toDouble()
            val milk =  viewModel.db.onlMilk + binding.lapte.text.toString().toDouble()
            val beans = viewModel.db.onlBeans +  binding.boabe.text.toString().toDouble()
            val cups = viewModel.db.onlCups + binding.pahare.text.toString().toDouble()
            val money = viewModel.db.onlMoney +  binding.bani.text.toString().toDouble()

            val resurse = ResurseOnline(water, milk, beans, cups, money)
            viewModel.dbRef.child("resources").setValue(resurse)
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