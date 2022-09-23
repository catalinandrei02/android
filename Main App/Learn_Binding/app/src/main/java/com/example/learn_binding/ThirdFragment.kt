package com.example.learn_binding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentThirdBinding



class ThirdFragment : Fragment() {
    private val viewModel: ResurseViewModel by activityViewModels()
    private var _binding: FragmentThirdBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        viewModel.getResources()
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
        binding.water.text = getString(R.string.check_w,viewModel.db.onlWater.toInt())
        binding.milk.text = getString(R.string.check_m,viewModel.db.onlMilk.toInt())
        binding.beans.text = getString(R.string.check_b,viewModel.db.onlBeans.toInt())
        binding.cups.text = getString(R.string.check_c,viewModel.db.onlCups.toInt())
        binding.money.text = getString(R.string.check_r,viewModel.db.onlMoney.toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}