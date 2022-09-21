package com.example.learn_binding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentThirdBinding
import com.google.firebase.database.ValueEventListener

class ThirdFragment : Fragment() {
    private val viewModel: ResurseViewModel by viewModels()
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

        val remainingWater = binding.water
        remainingWater.text = getString(R.string.check_w,viewModel.getWater().toInt())
        val remainingMilk = binding.milk
        remainingMilk.text = getString(R.string.check_m,viewModel.getMilk().toInt())
        val remainingBeans = binding.beans
        remainingBeans.text = getString(R.string.check_b,viewModel.getBeans().toInt())
        val remainingCups = binding.cups
        remainingCups.text = getString(R.string.check_c,viewModel.getCups().toInt())
        val remainingMoney = binding.money
        remainingMoney.text = getString(R.string.check_r,viewModel.getMoney().toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}