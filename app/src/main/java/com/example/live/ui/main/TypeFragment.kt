package com.example.live.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.live.R
import com.example.live.databinding.TypeFragmentBinding

class TypeFragment : Fragment() {
    private lateinit var viewModel: TypeViewModel
    private var _binding: TypeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TypeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(TypeViewModel::class.java)
        binding.viewModel = viewModel
        binding.typeItemList.layoutManager = LinearLayoutManager(this.requireContext())
        binding.typeList.adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_list_item_1,
            viewModel.typeListName
        )
        viewModel.chosenTypePosition.postValue(0)

        binding.typeList.setOnItemClickListener { _, _, position, _ ->
            viewModel.chosenTypePosition.postValue(position)
        }

        viewModel.findGoodsByCategory.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            if (result != null) {
                Log.e("result", result.toString())
                viewModel.itemsList = result.content
            } else
                viewModel.itemsList = mutableListOf()
            binding.typeItemList.adapter = TypeAdapter(context, viewModel.itemsList)
            Log.e("type", viewModel.itemsList.toString())
        }

        viewModel.getIntoDetailId.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_typeFragment_to_detailFragment,
                bundleOf("goodsId" to viewModel.getIntoDetailId.value!!.toLong())
            )
        }

        viewModel.getIntoLiveId.observe(viewLifecycleOwner) {
            //跳转到直播界面
        }

        binding.typeSearch.setOnClickListener {
            findNavController().navigate(R.id.action_typeFragment_to_searchFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}