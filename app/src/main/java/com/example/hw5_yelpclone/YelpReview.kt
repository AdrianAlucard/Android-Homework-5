package com.example.hw5_yelpclone

data class YelpReview(val businessName: String,
                      val reviewCount: Int,
                      val rating: Float,
                      val distance: String,
                      val address: String,
                      val category: String,
                      val imageURL: String,
                      val price: String)