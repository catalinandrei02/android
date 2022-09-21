package com.example.learn_binding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth


class FirstFragment : Fragment() {

    //am creeat un viewModel
    private val viewModel: ResurseViewModel by viewModels()
    private var _binding: FragmentFirstBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            //Am facut legatura intre viewModel din XML si clasa viewModel
            this.vm = viewModel
        }
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.fill.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_fragmentFourth)
        }
        binding.remaining.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_thirdFragment)
        }

        binding.take.setOnClickListener {
            Toast.makeText(context, "I gave you ${viewModel.getMoney()} RON", Toast.LENGTH_SHORT).show()
            viewModel.setMoney()
        }
        binding.logout.setOnClickListener{
            auth.signOut()
            findNavController().navigate(R.id.action_FirstFragment_to_welcomeFragment)
            Toast.makeText(context, "Logging you out!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}