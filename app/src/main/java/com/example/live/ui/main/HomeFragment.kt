package com.example.live.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.live.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    private val getPictureLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            try {
                val bitmap =
                    BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(it))
                binding.imageView4.setImageBitmap(bitmap)
                viewModel.imagesList = listOf<Bitmap>(bitmap)
                viewModel.upload()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private lateinit var viewModel: HomeViewModel
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
        //binding.homeTypeTab.layoutManager = LinearLayoutManager(this.requireContext())
        //binding.homeTypeTab.adapter = ArrayAdapter(this.requireContext(), R.layout.simple_list_item_1, viewModel.typeListName)
        viewModel.chosenTypePosition.postValue(0)













        binding.homeSearch.setOnClickListener {
            //findNavController().navigate(R.id.action_homeFragment_to_searchFragment)

            //val intent = Intent()
            //intent.action = Intent.ACTION_GET_CONTENT
            //intent.type = "image/*"
            //startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
            getPictureLauncher.launch("image/*")
        }

        viewModel.upLoadResponse.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            Log.e("result", result.toString())
        }
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && data != null){
            try {
                val bitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(
                    data.data!!
                ))
                binding.imageView4.setImageBitmap(bitmap)
                viewModel.imagesList = listOf<Bitmap>(bitmap)
                viewModel.upload()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}