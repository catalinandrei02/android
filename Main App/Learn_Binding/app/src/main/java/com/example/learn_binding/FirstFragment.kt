package com.example.learn_binding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.learn_binding.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth


class FirstFragment : Fragment() {

    //I made a shared viewModel so I could store and access data from across fragments.
    private val viewModel: ResurseViewModel by activityViewModels()
    private var _binding: FragmentFirstBinding? = null
    //I initialized the FireBase Auth, I'm using it to log in and register
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        viewModel.getResources()
        //I instantiated auth
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFirst.setOnClickListener {
            //navigates to second fragment (where you buy the coffe)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.fill.setOnClickListener {
            //navigates to fourth fragment (where you add new data to the database)
            findNavController().navigate(R.id.action_FirstFragment_to_fragmentFourth)
        }
        binding.remaining.setOnClickListener {
            //navigates to the third fragment (where you can check the data from the database)
            findNavController().navigate(R.id.action_FirstFragment_to_thirdFragment)
        }

        binding.take.setOnClickListener {
            //it gives you the value "money" from database
            Toast.makeText(context, "I gave you ${viewModel.db.onlMoney.toInt()} RON", Toast.LENGTH_SHORT).show()
            //set's "money" value to 0 in the database
            viewModel.setMoney()
            viewModel.getResources()
        }
        binding.logout.setOnClickListener{
            //it's using frebase's library to sing you out
            auth.signOut()
            //takes you to the welcome screen (login,register)
            findNavController().navigate(R.id.after_logout)
            Toast.makeText(context, "Logging you out!", Toast.LENGTH_LONG).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}