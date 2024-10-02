package com.example.photowardrobe.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photowardrobe.Models.Images
import com.example.photowardrobe.Models.Photo
import com.example.photowardrobe.Repository.ImageRepository
import com.example.photowardrobe.Utils.Resource
import kotlinx.coroutines.launch

class PhotosViewModel(
    private val repository: ImageRepository = ImageRepository()
) : ViewModel() {

    private val _naturalImage = MutableLiveData<Resource<Images>>()
    val naturalImage: LiveData<Resource<Images>> get() = _naturalImage

    private val _foodImage = MutableLiveData<Resource<Images>>()
    val foodImage: LiveData<Resource<Images>> get() = _foodImage

    private val _seaImage = MutableLiveData<Resource<Images>>()
    val seaImage: LiveData<Resource<Images>> get() = _seaImage

    private val _starImage = MutableLiveData<Resource<Images>>()
    val starImage: LiveData<Resource<Images>> get() = _starImage

    private val _carImage = MutableLiveData<Resource<Images>>()
    val carImage: LiveData<Resource<Images>> get() = _carImage

    private val _bikeImage = MutableLiveData<Resource<Images>>()
    val bikeImage: LiveData<Resource<Images>> get() = _bikeImage

    private val _imageData = MutableLiveData<Resource<Photo>>()
    val imageData: LiveData<Resource<Photo>> get() = _imageData

    private val defaultPerPage = 10
    var page:Int=1;
    fun fetchNaturalPhotos() {
        _naturalImage.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = repository.fetchAllPhotos("nature", defaultPerPage,page)
                _naturalImage.value = result
            } catch (e: Exception) {
                _naturalImage.value = Resource.Error("Failed to fetch natural photos: ${e.message}")
            }
        }
    }

    fun fetchFoodPhotos() {
        _foodImage.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = repository.fetchAllPhotos("food", defaultPerPage,page)
                _foodImage.value = result
            } catch (e: Exception) {
                _foodImage.value = Resource.Error("Failed to fetch food photos: ${e.message}")
            }
        }
    }

    fun fetchSeaPhotos() {
        _seaImage.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = repository.fetchAllPhotos("sea", defaultPerPage,page)
                _seaImage.value = result
            } catch (e: Exception) {
                _seaImage.value = Resource.Error("Failed to fetch sea photos: ${e.message}")
            }
        }
    }

    fun fetchStarPhotos() {
        _starImage.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = repository.fetchAllPhotos("star", defaultPerPage,page)
                _starImage.value = result
            } catch (e: Exception) {
                _starImage.value = Resource.Error("Failed to fetch star photos: ${e.message}")
            }
        }
    }

    fun fetchCarPhotos() {
        _carImage.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = repository.fetchAllPhotos("car", defaultPerPage,page)
                _carImage.value = result
            } catch (e: Exception) {
                _carImage.value = Resource.Error("Failed to fetch car photos: ${e.message}")
            }
        }
    }

    fun fetchBikePhotos() {
        _bikeImage.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = repository.fetchAllPhotos("bike", defaultPerPage,page)
                _bikeImage.value = result
            } catch (e: Exception) {
                _bikeImage.value = Resource.Error("Failed to fetch bike photos: ${e.message}")
            }
        }
    }

    fun fetchDataByIdView(id: Int) { // Update the type if it's a String
        _imageData.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = repository.fetchDataById(id)
                _imageData.value = result
            } catch (e: Exception) {
                _imageData.value = Resource.Error("Failed to fetch photo by ID: ${e.message}")
            }
        }
    }
    private val _searchResults = MutableLiveData<Resource<Images>>()
    val searchResults: LiveData<Resource<Images>> get() = _searchResults
    fun searchPhotos(query: String,Page:Int) {
        _searchResults.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = repository.fetchAllPhotos(query, 30,Page)
                _searchResults.value = result
            } catch (e: Exception) {
                _searchResults.value = Resource.Error("Failed to search photos: ${e.message}")
            }
        }
    }
}
