package com.example.stressmeter.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stressmeter.MainActivity
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
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Get images from ViewModel and set up the grid view
        homeViewModel.imageList.observe(viewLifecycleOwner) { imagesToDisplay ->
            setupGridView(imagesToDisplay)
        }

        // Set up the click listener for "More Images" button
        binding.buttonMoreImages.setOnClickListener {
            // Call MainActivity's function to stop vibration and sound
            (activity as? MainActivity)?.stopVibrationAndSound()

            // Load the next set of images
            homeViewModel.loadNextImageSet()
        }
        return root
    }

    // Function to set up the grid view with the images
    private fun setupGridView(imagesToDisplay: List<Int>) {
        // Use the image adapter to populate the grid view
        val adapter = ImageAdapter(requireContext(), imagesToDisplay)
        binding.gridView.adapter = adapter

        // Set up click listener for grid items
        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedImageId = imagesToDisplay[position]
            // Launch ImageResponseActivity to show the larger image
            val intent = Intent(requireContext(), ImageResponseActivity::class.java)

            // Pass the selected image ID to the activity so that it can display and use it later
            intent.putExtra("image_res_id", selectedImageId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
