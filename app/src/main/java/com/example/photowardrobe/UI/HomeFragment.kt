package com.example.photowardrobe.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photowardrobe.Adapter.MyAdapter
import com.example.photowardrobe.Models.Photo
//import com.example.photowardrobe.adapter.MyAdapter
import com.example.photowardrobe.R
import com.example.photowardrobe.UI.PhotoDescriptionFragment
import com.example.photowardrobe.Utils.Resource
import com.example.photowardrobe.ViewModels.PhotosViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

//import com.example.photowardrobe.model.Photo
//import com.example.photowardrobe.viewmodel.PhotosViewModel
//import com.example.photowardrobe.wrapper.Resource

class HomeFragment : Fragment() {
    private lateinit var natureRecyclerView: RecyclerView
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var seaRecyclerView: RecyclerView
    private lateinit var starhotos: RecyclerView
    private lateinit var carPhotos: RecyclerView
    private lateinit var bikePhotos: RecyclerView
    private lateinit var natureAdapter: MyAdapter
    private lateinit var foodAdapter: MyAdapter
    private lateinit var seaAdapter: MyAdapter
    private lateinit var starAdapter: MyAdapter
    private lateinit var carAdapter: MyAdapter
    private lateinit var bikeAdapter: MyAdapter
    private lateinit var seachBtm:FloatingActionButton

    private lateinit var photosViewModel: PhotosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        photosViewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)

        natureRecyclerView = view.findViewById(R.id.natureImageRecyclerView)
        foodRecyclerView = view.findViewById(R.id.foodImageRecyclerView)
        seaRecyclerView = view.findViewById(R.id.beachImageRecyclerView)
        starhotos = view.findViewById(R.id.starImageRecyclerView)
        carPhotos = view.findViewById(R.id.carImageRecyclerView)
        bikePhotos = view.findViewById(R.id.bikeImageRecyclerView)

        // Initialize Adapters with onItemClicked lambda
        natureAdapter = MyAdapter(emptyList()) { photo ->
            onPhotoClicked(photo, "Nature")
        }
        foodAdapter = MyAdapter(emptyList()) { photo ->
            onPhotoClicked(photo, "Food")
        }
        seaAdapter = MyAdapter(emptyList()) { photo ->
            onPhotoClicked(photo, "Sea")
        }
        starAdapter = MyAdapter(emptyList()) { photo ->
            onPhotoClicked(photo, "Star")
        }
        carAdapter = MyAdapter(emptyList()) { photo ->
            onPhotoClicked(photo, "Car")
        }
        bikeAdapter = MyAdapter(emptyList()) { photo ->
            onPhotoClicked(photo, "Bike")
        }

        natureRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        foodRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        seaRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        starhotos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        carPhotos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bikePhotos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        natureRecyclerView.adapter = natureAdapter
        foodRecyclerView.adapter = foodAdapter
        seaRecyclerView.adapter = seaAdapter
        starhotos.adapter = starAdapter
        carPhotos.adapter = carAdapter
        bikePhotos.adapter = bikeAdapter

        photosViewModel.fetchNaturalPhotos()
        photosViewModel.fetchFoodPhotos()
        photosViewModel.fetchSeaPhotos()
        photosViewModel.fetchStarPhotos()
        photosViewModel.fetchCarPhotos()
        photosViewModel.fetchBikePhotos()
        seachBtm=view.findViewById(R.id.searchFab)

        seachBtm.setOnClickListener{
            val fragment = SearchFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit()
        }
        photosViewModel.naturalImage.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val photos = resource.data?.photos ?: emptyList()
                    natureAdapter.updateData(photos)
                }
                is Resource.Error -> {

                    Log.e("HomeFragment", "Error fetching natural photos: ${resource.message}")
                }
                is Resource.Loading -> {
                }
            }
        }

        photosViewModel.foodImage.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val photos = resource.data?.photos ?: emptyList()
                    foodAdapter.updateData(photos)
                }
                is Resource.Error -> {
                    Log.e("HomeFragment", "Error fetching food photos: ${resource.message}")
                }
                is Resource.Loading -> {

                }
            }
        }

        photosViewModel.seaImage.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val photos = resource.data?.photos ?: emptyList()
                    seaAdapter.updateData(photos)
                }
                is Resource.Error -> {
                    Log.e("HomeFragment", "Error fetching sea photos: ${resource.message}")
                }
                is Resource.Loading -> {
                    // Show loading state if needed
                }
            }
        }

        photosViewModel.starImage.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val photos = resource.data?.photos ?: emptyList()
                    starAdapter.updateData(photos)
                }
                is Resource.Error -> {
                    Log.e("HomeFragment", "Error fetching star photos: ${resource.message}")
                }
                is Resource.Loading -> {
                    // Show loading state if needed
                }
            }
        }

        photosViewModel.carImage.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val photos = resource.data?.photos ?: emptyList()
                    carAdapter.updateData(photos)
                }
                is Resource.Error -> {
                    Log.e("HomeFragment", "Error fetching car photos: ${resource.message}")
                }
                is Resource.Loading -> {
                    // Show loading state if needed
                }
            }
        }

        photosViewModel.bikeImage.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val photos = resource.data?.photos ?: emptyList()
                    bikeAdapter.updateData(photos)
                }
                is Resource.Error -> {
                    Log.e("HomeFragment", "Error fetching bike photos: ${resource.message}")
                }
                is Resource.Loading -> {
                    // Show loading state if needed
                }
            }
        }

        return view
    }

    // Handle photo click events
    private fun onPhotoClicked(photo: Photo, category: String) {
        // Create a new instance of PhotoDescriptionFragment and pass the photo ID
        val fragment = PhotoDescriptionFragment().apply {
            arguments = Bundle().apply {
                putInt("photo_id", photo.id)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)  // Add transaction to back stack for navigation
            .commit()
    }



}

