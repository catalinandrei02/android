package com.example.projectmotivation.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.projectmotivation.R
import com.example.projectmotivation.content.ChatSecondFragment
import com.example.projectmotivation.model.User


@GlideModule
class UsersAdapter(private val context: Context, private val userList: ArrayList<User>):
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

        holder.userLayout.setOnClickListener {
            val activity = it.context as AppCompatActivity
            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout,ChatSecondFragment())
                .commitNow()

            val args = Bundle()
            args.putString("userId", user.userId)
            ChatSecondFragment().arguments = args
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textUserName: TextView = view.findViewById(R.id.tv_item_username)
        val imgUser: ImageView = view.findViewById(R.id.iv_item_userImage)
        val userLayout: ConstraintLayout = view.findViewById(R.id.layout_user)

    }

}