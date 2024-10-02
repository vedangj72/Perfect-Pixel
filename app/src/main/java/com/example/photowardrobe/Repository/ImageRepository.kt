package com.example.photowardrobe.Repository

import com.example.photowardrobe.Models.Images
import com.example.photowardrobe.Models.Photo
import com.example.photowardrobe.Network.ApiInsatance
import com.example.photowardrobe.Utils.Resource

class ImageRepository {

    private val api = ApiInsatance.api // Ensure correct reference to ApiInstance

    suspend fun fetchAllPhotos(query: String, perPage: Int,page:Int): Resource<Images> {
        return try {
            val response = api.getAllPhotos(query, perPage,page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message ?: "An unknown error occurred"}")
        }
    }

    suspend fun fetchDataById(id:Int): Resource<Photo> { // Changed id to String if the API expects a String
        return try {
            val response = api.getPhotoById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message ?: "An unknown error occurred"}")
        }
    }
}
