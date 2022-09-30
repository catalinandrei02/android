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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private var userList = ArrayList<User>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container,false)

        binding.rvChat.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

        runBlocking {
            launch(Dispatchers.IO){
                getUserList()
            }
        }

        val usersAdapter = UsersAdapter(this,userList)
        binding.rvChat.adapter = usersAdapter

        return binding.root
    }

    private fun getUserList(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val dbRef: DatabaseReference = Firebase.database.reference.child("users")
        dbRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue<User>()
                    if (user!!.userId == firebase.uid)
                        userList.add(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Failed to load users",Toast.LENGTH_SHORT).show()
            }

        })
    }

}