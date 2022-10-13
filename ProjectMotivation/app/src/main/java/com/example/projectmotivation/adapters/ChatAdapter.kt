package com.example.projectmotivation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.annotation.GlideModule
import com.example.projectmotivation.R
import com.example.projectmotivation.model.Chat
import com.example.projectmotivation.utils.Constants.Chat.MESSAGE_TYPE_LEFT
import com.example.projectmotivation.utils.Constants.Chat.MESSAGE_TYPE_RIGHT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@GlideModule
class ChatAdapter(private val chatList: ArrayList<Chat>):
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private lateinit var firebaseUser: FirebaseUser

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            ViewHolder(view)
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.txtUserName.text = chat.message
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtUserName: TextView = view.findViewById(R.id.tvMessage)
    }
    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        return if (chatList[position].senderId == firebaseUser.uid) {
            MESSAGE_TYPE_RIGHT
        } else {
            MESSAGE_TYPE_LEFT
        }
    }
}