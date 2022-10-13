package com.example.spotifyp3.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifyp3.R

class ListAdapter(private val context: Context, private val arrayList: Array<String>) : BaseAdapter() {
    private lateinit var item: TextView
    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_listview_row, parent, false)
        item = layout.findViewById(R.id.tv_list_item)
        item.text = arrayList[position]
        return layout
    }
}