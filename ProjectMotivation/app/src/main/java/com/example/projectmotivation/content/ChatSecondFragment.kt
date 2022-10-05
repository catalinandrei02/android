package com.example.projectmotivation.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.projectmotivation.databinding.FragmentChatSecondBinding
import com.example.projectmotivation.model.User
import com.example.projectmotivation.utils.Constants
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChatSecondFragment : Fragment() {

    private lateinit var binding: FragmentChatSecondBinding
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatSecondBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserChat()
    }

    private fun getUserChat(){

        val args = arguments
        val userId = args?.getString("userId")

        firebaseUser = Firebase.auth.currentUser!!
        dbRef = Firebase.database.reference.child(Constants.Url.USER).child(userId!!)
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Failed to load user chat!", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue<User>()
                    binding.tvChatUsername.text = user?.userName
                    if (user != null && user.userImage != "") {
                        Glide.with(requireContext()).load(user.userImage).into(binding.ivChatUserImage)
                    }
                }
            }
        })
    }
}