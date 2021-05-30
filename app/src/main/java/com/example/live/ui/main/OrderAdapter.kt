package com.example.live.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.live.databinding.ItemOrderListBinding

class OrderAdapter(
    private var context: Context?,
    private var paramList: List<OrderViewModel.GoodsList>
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private val orderViewModel = ViewModelProvider(context as FragmentActivity).get(
        OrderViewModel::class.java
    )

    inner class ViewHolder(val binding: ItemOrderListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        viewHolder.binding.orderItemMinus.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val param = paramList[position]
            if (param.quantity != 0L) {
                param.quantity = param.quantity - 1
                viewHolder.binding.itemOrderQuantity.text = param.quantity.toString()
                orderViewModel.goodsList[position].quantity = param.quantity
                orderViewModel.notifySum()
            }
        }
        viewHolder.binding.orderItemPlus.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val param = paramList[position]
            if (param.quantity < param.maxQuantity) {
                param.quantity = param.quantity + 1
                viewHolder.binding.itemOrderQuantity.text = param.quantity.toString()
                orderViewModel.goodsList[position].quantity = param.quantity
                orderViewModel.notifySum()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val param = paramList[position]
        holder.binding.itemOrderTitle.text = param.goodsId.toString()
        holder.binding.itemOrderQuantity.text = param.quantity.toString()
        holder.binding.itemOrderPrice.text = param.goodsPrice.toString()
        Glide.with(context!!).load(param.goodsPhoto).into(holder.binding.itemOrderPic)
        //TODO(实际需要修改)
    }

    override fun getItemCount() = paramList.size
}