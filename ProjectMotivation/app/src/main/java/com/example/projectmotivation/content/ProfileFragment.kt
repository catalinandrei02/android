package com.example.projectmotivation.content

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.projectmotivation.R
import com.example.projectmotivation.databinding.FragmentProfileBinding
import com.example.projectmotivation.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    val filePath: Uri? = null
    private val pickImgRequest: Int = 2022

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        auth = Firebase.auth

        getUser()

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_mainFragment_to_welcomeFragment)
            Toast.makeText(context, "Logging you out!", Toast.LENGTH_LONG).show()
        }

        return binding.root
    }
    private fun getUser(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val dbRef: DatabaseReference = Firebase.database.reference.child("users").child(firebase.uid)
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
             binding.tvItemUsername.text = user!!.userName
                if (user.userImage == ""){
                    binding.ivUserImage.setImageResource(R.drawable.blank_profile)
                } else {
                    Glide.with(context!!).load(user.userImage).into(binding.ivUserImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Failed to load users",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun chooseImg(){

    }
}