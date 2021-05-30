package com.example.live.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.live.databinding.ItemCartListBinding
import com.example.live.logic.model.CartInfo

class CartAdapter(
    context: Context?,
    private var paramList: List<CartInfo>
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private val cartViewModel = ViewModelProvider(context as FragmentActivity).get(
        CartViewModel::class.java
    )

    inner class ViewHolder(val binding: ItemCartListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        viewHolder.binding.itemCartCheck.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val param = paramList[position]
            param.isChecked = viewHolder.binding.itemCartCheck.isChecked
            cartViewModel.goodsList[position].isChecked = viewHolder.binding.itemCartCheck.isChecked
            cartViewModel.notifySum()
        }
        viewHolder.binding.cartItemMinus.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val param = paramList[position]
            if (param.quantity != 0L) {
                param.quantity = param.quantity - 1
                viewHolder.binding.itemCartQuantity.text = param.quantity.toString()
                cartViewModel.goodsList[position].quantity = param.quantity
                cartViewModel.notifySum()
            }
        }
        viewHolder.binding.cartItemPlus.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val param = paramList[position]
            if (param.quantity < param.maxQuantity) {
                param.quantity = param.quantity + 1
                viewHolder.binding.itemCartQuantity.text = param.quantity.toString()
                cartViewModel.goodsList[position].quantity = param.quantity
                cartViewModel.notifySum()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val param = paramList[position]
        holder.binding.itemCartTitle.text = param.goodsId.toString()
        holder.binding.itemCartQuantity.text = param.quantity.toString()
        holder.binding.itemCartCheck.isChecked = param.isChecked
        //TODO(实际需要修改)
    }

    override fun getItemCount() = paramList.size
}