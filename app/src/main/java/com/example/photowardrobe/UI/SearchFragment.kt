package com.example.photowardrobe.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photowardrobe.Adapter.MyAdapter
import com.example.photowardrobe.R
import com.example.photowardrobe.Utils.Resource
import com.example.photowardrobe.ViewModels.PhotosViewModel

class SearchFragment : Fragment() {

    private lateinit var searchQuery: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var photosViewModel: PhotosViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var searchBtn: ImageView
    private lateinit var PageButtonfront: ImageView
    private lateinit var PageButtonback: ImageView

    private var isLoading = false
    private var currentPage = 1
    private val maxPages = 5
    private var currentQuery = "Random"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchQuery = view.findViewById(R.id.searchEditText)
        recyclerView = view.findViewById(R.id.searchRecycerView)
        progressBar = view.findViewById(R.id.searchPBar)
        searchBtn = view.findViewById(R.id.searchBtn)
        PageButtonfront = view.findViewById(R.id.paginationButtonfront)
        PageButtonback = view.findViewById(R.id.paginationButtonback)

        setupRecyclerView()
        setupViewModel()
        setupListeners()

        // Initial fetch
        photosViewModel.searchPhotos(currentQuery, currentPage)
    }

    private fun setupRecyclerView() {
        adapter = MyAdapter(emptyList()) { photo ->
            val fragment = PhotoDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putInt("photo_id", photo.id)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        // Add a scroll listener for pagination
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (!recyclerView.canScrollVertically(1) && !isLoading && currentPage < maxPages) {
//                    currentPage++
//                    photosViewModel.searchPhotos(currentQuery, currentPage)
//                }
//            }
//        })
    }

    private fun setupViewModel() {
        photosViewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)
        photosViewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    isLoading = false
                    val photos = resource.data?.photos ?: emptyList()
                    adapter.updateData(photos)
                    recyclerView.scrollToPosition(0)
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    isLoading = false
                    Toast.makeText(requireContext(), "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    isLoading = true
                }
            }
        }
    }

    private fun setupListeners() {
        searchBtn.setOnClickListener {
            val query = searchQuery.text.toString().trim()
            if (query.isNotEmpty() && query != currentQuery) {
                currentQuery = query
                currentPage = 1 // Reset to the first page
                adapter.updateData(emptyList()) // Clear current data
                photosViewModel.searchPhotos(query, currentPage)
                updatePaginationButtons() // Update buttons state
            }
        }

        PageButtonfront.setOnClickListener {
            if (currentPage < maxPages) {
                currentPage++
                photosViewModel.searchPhotos(currentQuery, currentPage)
                updatePaginationButtons()
            }
        }

        PageButtonback.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                photosViewModel.searchPhotos(currentQuery, currentPage)
                updatePaginationButtons()
            }
        }

        updatePaginationButtons() // Initial button state update
    }

    private fun updatePaginationButtons() {
        PageButtonback.isEnabled = currentPage > 1
        PageButtonfront.isEnabled = currentPage < maxPages
    }
}
