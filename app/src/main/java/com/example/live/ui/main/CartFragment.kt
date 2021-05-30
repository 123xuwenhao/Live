package com.example.live.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.live.R
import com.example.live.databinding.CartFragmentBinding

class CartFragment : Fragment() {
    private lateinit var viewModel: CartViewModel
    private var _binding: CartFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
        //TODO(由fragment代管后Adapter无法拿到)
        binding.viewModel = viewModel
        binding.cartList.layoutManager = LinearLayoutManager(this.requireContext())
        viewModel.getAllCarts()

        viewModel.findAllCartsResponse.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            if (!result.isNullOrEmpty()) {
                viewModel.goodsList = viewModel.freshGoodsList(result)
                binding.cartList.adapter = CartAdapter(context, viewModel.goodsList)
                viewModel.notifySum()
            } else {
                viewModel.goodsList = listOf()
                binding.cartList.adapter = CartAdapter(context, viewModel.goodsList)
                viewModel.notifySum()
            }
        }

        binding.shoppingCartWholeCheck.setOnClickListener {
            if (!viewModel.goodsList.isNullOrEmpty()) {
                if (binding.shoppingCartWholeCheck.isChecked) {
                    viewModel.goodsList.forEach { it.isChecked = true }
                    binding.cartList.adapter = CartAdapter(context, viewModel.goodsList)
                    viewModel.notifySum()
                } else {
                    viewModel.goodsList.forEach { it.isChecked = false }
                    binding.cartList.adapter = CartAdapter(context, viewModel.goodsList)
                    viewModel.notifySum()
                }
            }
        }

        viewModel.sum.observe(viewLifecycleOwner) {
            binding.cartPrice.text = "合计：$it"
        }

        viewModel.count.observe(viewLifecycleOwner) {
            binding.cartSettle.text = "去结算($it)"
        }

        binding.cartSettle.setOnClickListener {
            findNavController().navigate(
                R.id.action_cartFragment_to_orderFragment,
                bundleOf("goodsList" to viewModel.goodsList)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}