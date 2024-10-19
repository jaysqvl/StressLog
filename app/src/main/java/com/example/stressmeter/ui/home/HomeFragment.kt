package com.example.stressmeter.ui.home

import HomeViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stressmeter.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Observe LiveData from ViewModel to get images to display
        homeViewModel.imageList.observe(viewLifecycleOwner) { imagesToDisplay ->
            setupGridView(imagesToDisplay)
        }

        // Load more images on button click
        binding.buttonMoreImages.setOnClickListener {
            homeViewModel.loadNextImageSet()
        }

        return root
    }

    private fun setupGridView(imagesToDisplay: List<Int>) {
        val adapter = ImageAdapter(requireContext(), imagesToDisplay)
        binding.gridView.adapter = adapter

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedImageId = imagesToDisplay[position]
            // Launch ImageResponseActivity to show the larger image
            val intent = Intent(requireContext(), ImageResponseActivity::class.java)
            intent.putExtra("image_res_id", selectedImageId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
