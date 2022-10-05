package com.example.projectmotivation.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmotivation.adapters.UsersAdapter
import com.example.projectmotivation.databinding.FragmentChatBinding
import com.example.projectmotivation.model.User
import com.example.projectmotivation.utils.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayoutManager()
    }

    private fun setupLayoutManager() {
        binding.rvChat.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        val usersAdapter = context?.let { UsersAdapter(it,userList) }
        binding.rvChat.adapter = usersAdapter
    }

    private fun getUserList(){
        val firebase = Firebase.auth.currentUser
        val dbRef = Firebase.database.reference.child(Constants.Url.USER)
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue<User>()
                    if (user != null && firebase != null) {
                        if (user.userId != firebase.uid)
                            userList.add(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Failed to load users",Toast.LENGTH_SHORT).show()
            }

        })
    }

}