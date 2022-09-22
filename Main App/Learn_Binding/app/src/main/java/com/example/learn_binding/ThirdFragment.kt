package com.example.learn_binding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentThirdBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThirdFragment : Fragment() {
    private val viewModel: ResurseViewModel by viewModels()
    private val resourceCollectionRef = Firebase.firestore.collection("resurse")
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            this.vm = viewModel
        }
        binding.exit.setOnClickListener {
            findNavController().navigate(R.id.action_thirdFragment_to_FirstFragment)
        }


        binding.milk.text = getString(R.string.check_m,viewModel.getMilk().toInt())
        binding.beans.text = getString(R.string.check_b,viewModel.getBeans().toInt())
        binding.cups.text = getString(R.string.check_c,viewModel.getCups().toInt())
        binding.money.text = getString(R.string.check_r,viewModel.getMoney().toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun retrieveResources() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = resourceCollectionRef.get()
            val sb = StringBuilder()
           /* for (document in querySnapshot){
                val resurse = document.toObject<Resurse>()
                sb.append("$resurse\n")
            }*/
        withContext(Dispatchers.Main){
            binding.water.text = getString(R.string.check_w,sb.toString().toInt())
        }
        } catch (e: Exception) {

        }
    }
}