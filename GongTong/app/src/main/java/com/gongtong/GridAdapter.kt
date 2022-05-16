package com.gongtong

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import com.gongtong.databinding.ItemRecyclerBinding
import java.util.*

class GridAdapter(
    val onItemClicked: (GridData) -> Unit) : ListAdapter<GridData, GridAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(griddata: GridData) {
            binding.name.text = griddata.name
            Log.d("이미지 주소",griddata.image_url )
            // glide로 이미지 불러오기;
            if (griddata.image_url.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(griddata.image_url)
                    .into(binding.thumbnailImageView)
            }
            binding.root.setOnClickListener {
                onItemClicked(griddata)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GridData>() {
            override fun areItemsTheSame(oldItem: GridData, newItem: GridData): Boolean {
                // 현재 노출하고 있는 아이템과 새로운 아이템이 같은지 비교;
                return oldItem.image_url == newItem.image_url
            }
            override fun areContentsTheSame(oldItem: GridData, newItem: GridData): Boolean {
                // equals 비교;
                return oldItem == newItem
            }
        }
    }
}