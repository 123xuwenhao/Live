package com.example.live.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.live.databinding.OrderFragmentBinding
import com.example.live.logic.model.CartInfo
import com.google.android.material.snackbar.Snackbar

class OrderFragment : Fragment() {
    private lateinit var viewModel: OrderViewModel
    private var _binding: OrderFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OrderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)
        binding.viewModel = viewModel
        binding.orderGoodsList.layoutManager = LinearLayoutManager(this.requireContext())

        val goodsId = arguments?.get("goodsId") as Long?
        val goodsName = arguments?.get("goodsName") as String?
        val goodsPrice = arguments?.get("goodsPrice") as String?
        val goodsPhoto = arguments?.get("goodsPhoto") as String?
        val goodsMaxQuantity = arguments?.get("goodsMaxQuantity") as Long
        val goodsFromCart = arguments?.get("goodsList") as List<CartInfo>?

        when {
            goodsId != null -> {
                viewModel.goodsList = listOf(
                    OrderViewModel.GoodsList(
                        goodsId,
                        goodsName,
                        goodsPrice,
                        goodsPhoto,
                        1,
                        goodsMaxQuantity
                    )
                )
                Log.e("goods", viewModel.goodsList[0].toString())
            }
            goodsFromCart != null -> {
                viewModel.goodsList = goodsFromCart.map { cartInfo: CartInfo ->
                    OrderViewModel.GoodsList(
                        cartInfo.goodsId,
                        cartInfo.name,
                        cartInfo.price,
                        cartInfo.photoPath,
                        cartInfo.quantity,
                        cartInfo.maxQuantity
                    )
                }
            }
            else -> {
                viewModel.goodsList = listOf()
            }
        }

        binding.orderGoodsList.adapter = OrderAdapter(context, viewModel.goodsList)
        viewModel.notifySum()

        binding.orderSubmit.setOnClickListener {
            viewModel.submitOrder()
        }

        viewModel.submitResponse.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            if (result != null) {
                Snackbar.make(requireView(), "订单发送成功", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(requireView(), "订单发送失败", Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.sumText.observe(viewLifecycleOwner) {
            binding.orderSum.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}