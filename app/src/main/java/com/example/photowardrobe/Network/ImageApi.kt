package com.example.photowardrobe.Network


import com.example.photowardrobe.Models.Images
import com.example.photowardrobe.Models.Photo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImageApi {
    @GET("search")
    suspend fun getAllPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int,
        @Query("page")page:Int
    ):Response<Images>

    @GET("photos/{id}")
    suspend fun getPhotoById(
        @Path("id") photoId: Int,
    ): Response<Photo>
}
