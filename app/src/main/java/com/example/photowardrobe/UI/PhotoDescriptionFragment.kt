package com.example.photowardrobe.UI

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.photowardrobe.R
import com.example.photowardrobe.Utils.Resource
import com.example.photowardrobe.ViewModels.PhotosViewModel

import com.google.android.material.snackbar.Snackbar
import java.io.File

class PhotoDescriptionFragment : Fragment() {

    private lateinit var photoTitleText: TextView
    private lateinit var photographerNameText: TextView
    private lateinit var photoCardView: CardView
    private lateinit var photoImage: ImageView
    private lateinit var previewImage: ImageView
    private lateinit var cancelBtn: ImageView
    private lateinit var previewBtn: Button
    private lateinit var downloadBtn: Button
    private lateinit var url: String

    private lateinit var photosViewModel: PhotosViewModel

    private var photoId: Int? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_description, container, false)

        initializeViews(view)
        setupViewModel()
        setupListeners()
        loadPhotoData()

        return view
    }

    private fun initializeViews(view: View) {
        photoTitleText = view.findViewById(R.id.photoTitleTextView)
        photographerNameText = view.findViewById(R.id.photographerNameTextView)
        photoImage = view.findViewById(R.id.photoImageView)
        previewImage = view.findViewById(R.id.previewImage)
        cancelBtn = view.findViewById(R.id.cancelBtn)
        previewBtn = view.findViewById(R.id.previewBtn)
        photoCardView = view.findViewById(R.id.photoCardView)
        downloadBtn = view.findViewById(R.id.downloadBtn)

        photoCardView.visibility = View.VISIBLE
    }

    private fun setupViewModel() {
        photosViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]
    }

    private fun setupListeners() {
        previewBtn.setOnClickListener { showPreview() }
        cancelBtn.setOnClickListener { hidePreview() }
        previewImage.setOnClickListener { hidePreview() }
        downloadBtn.setOnClickListener {
            downloadPhoto()
        }
    }

    private fun loadPhotoData() {
        photoId = arguments?.getInt("photo_id")
        photoId?.let { id ->
            photosViewModel.fetchDataByIdView(id)
            observePhotoData()
        } ?: showErrorMessage("No photo ID provided")
    }

    private fun showPreview() {
        previewImage.visibility = View.VISIBLE
        cancelBtn.visibility = View.VISIBLE
        photoCardView.visibility = View.GONE
    }

    private fun hidePreview() {
        previewImage.visibility = View.GONE
        cancelBtn.visibility = View.GONE
        photoCardView.visibility = View.VISIBLE
    }

    private fun observePhotoData() {
        photosViewModel.imageData.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Success -> {
                    val photo = resource.data
                    photo?.let {
                        url = it.src.portrait
                        photoTitleText.text = it.alt
                        photographerNameText.text = it.photographer
                        loadImage(it.src.original, photoImage)
                        loadImage(it.src.portrait, previewImage)
                        Log.d("PhotoDescriptionFragment", "Photo data loaded successfully")
                    }
                }
                is Resource.Error -> {
                    Log.e("PhotoDescriptionFragment", "Error fetching photo: ${resource.message}")
                    showErrorMessage("Error loading photo: ${resource.message}")
                }
                is Resource.Loading -> {
                    // Show loading state if needed
                }
            }
        }
    }

    private fun loadImage(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_launcher_background)
            .into(imageView)
    }

    private fun showErrorMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    private fun downloadPhoto() {
        try {
            val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(url)
            val request = DownloadManager.Request(downloadUri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Photo Download")
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "downloaded_photo.jpg")
            downloadManager.enqueue(request)
            Toast.makeText(requireContext(), "Image Download Started", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Image Download Failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        photosViewModel.imageData.removeObservers(viewLifecycleOwner)
    }
}
