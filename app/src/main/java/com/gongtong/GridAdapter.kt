package com.gongtong

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gongtong.databinding.ActivityHomeBinding
import com.gongtong.databinding.ActivityHomeBinding.inflate
import com.gongtong.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat
import java.util.*

class GridAdapter(val binding: MutableList<GridData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listData = mutableListOf<GridData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GridViewHolder(ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as GridViewHolder).binding
        binding.itemData.text = datas[position]
            }
}

class GridViewHolder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root)
//ddddddddd