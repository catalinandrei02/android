package com.example.projectmotivation.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmotivation.R
import com.example.projectmotivation.adapters.ChatAdapter
import com.example.projectmotivation.databinding.FragmentChatSecondBinding
import com.example.projectmotivation.model.Chat
import com.example.projectmotivation.model.User
import com.example.projectmotivation.utils.Constants
import com.example.projectmotivation.utils.Constants.Url.CHAT
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
    private var chatList = ArrayList<Chat>()

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
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_chatSecondFragment_to_mainFragment)
        }
    }

    private fun getUserChat(){

        val userId = arguments?.getString("userId")

        firebaseUser = Firebase.auth.currentUser!!
        dbRef = userId?.let { Firebase.database.reference.child(Constants.Url.USER_CHAT).child(it) }!!
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Failed to load user chat!", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                user?.let {
                    binding.tvChatUsername.text = user.name
                    if (user.userImage == ""){
                        binding.ivChatUserImage.setImageResource(R.drawable.blank_profile)
                    } else {
                        context?.let { it1 -> Glide.with(it1).load(user.userImage.toString()).into(binding.ivChatUserImage) }
                    }
                }
            }
        })
        binding.btnSend.setOnClickListener {
            val message = binding.etChatSecondSend.text.toString().trim()
            if (message.isEmpty()){
                Toast.makeText(context, "Message is empty!", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(firebaseUser.uid,userId,message)
                binding.etChatSecondSend.setText("")
            }
            readMessage(firebaseUser.uid, userId)
        }
        readMessage(firebaseUser.uid, userId)
    }
    private fun setupLayoutManager() {
        binding.rvChatSecond.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        val chatAdapter = ChatAdapter(chatList)
        binding.rvChatSecond.adapter = chatAdapter
    }
    private fun sendMessage(senderId: String, receiverId: String, message: String){
        dbRef = Firebase.database.reference

        val hashMap: HashMap<String,String> = HashMap()
        hashMap["senderId"] = senderId
        hashMap["receiverId"] = receiverId
        hashMap["message"] = message
        dbRef.child("Chat").push().setValue(hashMap)

    }
    private fun readMessage(senderId: String, receiverId: String) {
        val dbRef = Firebase.database.reference.child(CHAT)
       dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue<Chat>()
                    if (chat!!.senderId == senderId && chat.receiverId == receiverId ||
                        chat.senderId == receiverId && chat.receiverId == senderId
                    ) {
                        chatList.add(chat)
                    }
                    setupLayoutManager()
                }
            }
        })
    }

}