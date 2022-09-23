package com.example.learn_binding


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {
    //viewModel created
    private val viewModel: ResurseViewModel by activityViewModels() //shared viewModel
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        //gets resources
        viewModel.getResources()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //legatura viewModel kt si xml
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            this.vm = viewModel
        }

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.expresso.setOnClickListener {
            viewModel.makeOnlineCoffe(250.0, 0.0, 16.0, 15.0)
        }
        binding.latte.setOnClickListener {
            viewModel.makeOnlineCoffe(350.0, 75.0, 20.0, 13.8)
        }
        binding.cappuccino.setOnClickListener {

            viewModel.makeOnlineCoffe(200.0, 100.0, 10.0, 16.5)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}