package com.example.projectmotivation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.projectmotivation.R
import com.example.projectmotivation.content.ChatFragment
import com.example.projectmotivation.model.User

@GlideModule
class UsersAdapter(private val context: ChatFragment, private val userList: ArrayList<User>):
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.textUserName.text = user.userName
        Glide.with(context).load(user.userImage).into(holder.imgUser)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textUserName: TextView = view.findViewById(R.id.tv_item_username)
        val textStatus: TextView = view.findViewById(R.id.tv_item_status) //TODO: Implement User Status
        val imgUser: ImageView = view.findViewById(R.id.iv_userImage)
    }
}