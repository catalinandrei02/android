package com.example.spotifyp3.adapters


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
import com.example.spotifyp3.R
import com.example.spotifyp3.data.UserEntity


//@GlideModule
class UserAdapter(private val context: Context, userList: ArrayList<UserEntity>):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var listener: ((String?)->Unit)? = null
    private var userList = emptyList<UserEntity>()

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
        holder.textUserGender.text = user.gender
        holder.textUserId.text = user.id.toString()
        if (user.image == "") {
            Glide.with(context).load(R.drawable.blank_profile).into(holder.imgUser)
        } else {
            Glide.with(context).load(user.image).into(holder.imgUser)
        }

        holder.userLayout.setOnClickListener {
            listener?.invoke(user.image)
            listener?.invoke(user.name)
        }

    }

    fun setData(user: List<UserEntity>){
        this.userList = user
        notifyDataSetChanged()
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textUserName: TextView = view.findViewById(R.id.tv_item_username)
        val imgUser: ImageView = view.findViewById(R.id.iv_item_userImage)
        val textUserId: TextView = view.findViewById(R.id.tv_item_userid)
        val textUserGender: TextView = view.findViewById(R.id.tv_item_usergender)
        val userLayout: ConstraintLayout = view.findViewById(R.id.layout_user)
    }

}