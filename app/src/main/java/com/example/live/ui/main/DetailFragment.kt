package com.example.live.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.live.R
import com.example.live.databinding.DetailFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        binding.viewModel = viewModel
        val getIntoDetailId = arguments?.get("goodsId")
        if (getIntoDetailId != null)
            viewModel.goodsId.postValue(getIntoDetailId as Long)
        else
            viewModel.goodsId.value = 0

        viewModel.detailResponse.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            if (result != null && result.goodsId == viewModel.goodsId.value) {
                viewModel.apply {
                    vendorId.value = result.vendorId
                    name.value = result.name
                    price.value = result.price
                    category.value = result.category
                    soldCount.value = result.soldCount
                    maxQuantity.value = result.maxQuantity
                    photoPath.value = result.photoPath
                    descriptionPath.value = result.descriptionPath
                    specPath.value = result.specPath
                    deliveryType.value = result.deliveryType
                    displayType.value = result.displayType
                }
                binding.detailPrice.text = viewModel.price.value
                binding.detailTitle.text = viewModel.name.value
            }
        }

        viewModel.photoPath.observe(viewLifecycleOwner) {
            Glide.with(this).load(viewModel.photoPath.value).override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                .fitCenter().into(binding.detailFirstPic)
        }

        viewModel.descriptionPath.observe(viewLifecycleOwner) {
            Glide.with(this).load(viewModel.descriptionPath.value)
                .override(SIZE_ORIGINAL, SIZE_ORIGINAL).fitCenter().into(binding.detailSecondPic)
        }

        viewModel.addCartResponse.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            if (result != null)
                Snackbar.make(requireView(), "已加入购物车", Snackbar.LENGTH_SHORT).show()
            else
                Snackbar.make(requireView(), "添加失败", Snackbar.LENGTH_SHORT).show()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> binding.detailScrollView.fullScroll(NestedScrollView.FOCUS_UP)
                    1 -> binding.detailScrollView.smoothScrollTo(0, binding.detailViewDetail.top)
                    else -> {
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }
        })

        binding.detailImageCart.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_cartFragment)
        }

        binding.detailCart.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_cartFragment)
        }

        binding.detailImageShare.setOnClickListener {
            Snackbar.make(requireView(), "clicked Share Button.", Snackbar.LENGTH_SHORT).show()
        }

        binding.detailShare.setOnClickListener {
            Snackbar.make(requireView(), "clicked Share Button.", Snackbar.LENGTH_SHORT).show()
        }

        binding.detailAddCart.setOnClickListener {
            viewModel.addGoodsToCart()
        }

        binding.detailAddBuy.setOnClickListener {
            findNavController().navigate(
                R.id.action_detailFragment_to_orderFragment,
                bundleOf(
                    "goodsId" to viewModel.goodsId.value,
                    "goodsName" to viewModel.name.value,
                    "goodsPrice" to viewModel.price.value,
                    "goodsPhoto" to viewModel.photoPath.value,
                    "goodsMaxQuantity" to viewModel.maxQuantity.value
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}