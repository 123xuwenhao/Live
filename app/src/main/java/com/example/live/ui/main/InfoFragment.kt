package com.example.live.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.live.R
import com.example.live.databinding.InfoFragmentBinding
import com.example.live.logic.DataManager

class InfoFragment : Fragment() {

    private lateinit var viewModel: InfoViewModel
    private var _binding: InfoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.userId.value = DataManager.loginId

        viewModel.getUserInfo.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            if (result != null) {
                binding.infoId.text = result.name
                Glide.with(this)
                    .load(result.photoPath)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(binding.infoHead)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}