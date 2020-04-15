package com.example.hw5_yelpclone

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpService {

    @GET("businesses/search")
    fun SearchYelp(@Query("term") searchTerm: String, @Query("location") location: String, @Header("Authorization") header: String): Call<YelpResults>

    @GET("businesses/search")
    fun SearchYelp(@Query("term") searchTerm: String, @Query("location") location: String, @Query("radius") radius: Int, @Header("Authorization") header: String): Call<YelpResults>

    @GET("businesses/search")
    fun SearchYelp(@Query("term") searchTerm: String, @Query("location") location: String, @Query("sort_by") sortBy: String, @Header("Authorization") header: String): Call<YelpResults>

    @GET("businesses/search")
    fun SearchYelp(@Query("term") searchTerm: String, @Query("location") location: String, @Query("sort_by") sortBy: String, @Query("sort_by") radius: Int, @Header("Authorization") header: String): Call<YelpResults>

}