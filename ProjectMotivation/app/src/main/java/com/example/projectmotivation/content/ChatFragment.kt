package com.example.projectmotivation.content

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmotivation.adapters.UsersAdapter
import com.example.projectmotivation.databinding.FragmentChatBinding
import com.example.projectmotivation.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private var userList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container,false)

        binding.rvChat.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

        getUserList()
        val usersAdapter = UsersAdapter(this,userList)
        binding.rvChat.adapter = usersAdapter

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        background()
    }
    private fun background(){
        val frameAnimation: AnimationDrawable = binding.animation.background as AnimationDrawable
        frameAnimation.setEnterFadeDuration(2500)
        frameAnimation.setExitFadeDuration(5000)
        frameAnimation.start()
    }
    private fun getUserList() = lifecycleScope.launch(Dispatchers.IO){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val dbRef: DatabaseReference = Firebase.database.reference.child("users")

        dbRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)
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