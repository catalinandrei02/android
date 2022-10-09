package com.example.projectmotivation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.projectmotivation.R
import com.example.projectmotivation.model.User


@GlideModule
class UsersAdapter(private val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    var listener: ((String?)->Unit)? = null

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.textUserName.text = user.name
        if (user.userImage == "") {
            Glide.with(context).load(R.drawable.blank_profile).into(holder.imgUser)
        } else {
            Glide.with(context).load(user.userImage).into(holder.imgUser)
        }

        holder.userLayout.setOnClickListener {
            listener?.invoke(user.userId)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textUserName: TextView = view.findViewById(R.id.tv_item_username)
        val imgUser: ImageView = view.findViewById(R.id.iv_item_userImage)
        val userLayout: ConstraintLayout = view.findViewById(R.id.layout_user)
    }
}