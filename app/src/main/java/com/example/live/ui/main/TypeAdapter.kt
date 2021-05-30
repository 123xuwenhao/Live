package com.example.live.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.live.databinding.ItemTypeListBinding
import com.example.live.logic.model.Good

class TypeAdapter(
    private var context: Context?,
    private val paramList: List<Good>
) : RecyclerView.Adapter<TypeAdapter.ViewHolder>() {
    private val typeViewModel = ViewModelProvider(context as FragmentActivity).get(
        TypeViewModel::class.java
    )

    inner class ViewHolder(val binding: ItemTypeListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val param = paramList[position]
            typeViewModel.getIntoDetailId.postValue(param.goodsId)
        }
        viewHolder.binding.itemTypeButton.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val param = paramList[position]
            typeViewModel.getIntoLiveId.postValue(param.goodsId)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val param = paramList[position]
        Glide.with(context!!).load(param.photoPath).into(holder.binding.itemTypeImage)
        holder.binding.itemTypeTitle.text = param.name
        holder.binding.itemTypePrice.text = param.price
    }

    override fun getItemCount() = paramList.size
}