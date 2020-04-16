package com.example.hw5_yelpclone

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpService {

    @GET("businesses/search")
    fun SearchYelp(@Query("term") searchTerm: String, @Query("location") location: String): Call<YelpResults>

    @GET("businesses/search")
    fun SearchYelp(@Query("term") searchTerm: String,
                   @Query("latitude") lat: Double,
                   @Query("longitude") long: Double,
                   @Query("radius") radius: Int): Call<YelpResults>
}