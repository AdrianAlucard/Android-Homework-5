package com.example.hw5_yelpclone

import java.net.URL

data class YelpResults(val businesses: List<YelpReview>)

data class YelpReview(val id: String,
                      val alias: String,
                      val name: String,
                      val image_url: String,
                      val is_closed: Boolean,
                      val url: URL,
                      val review_count: Int,
                      val categories: List<Category>,
                      val rating: Double,
                      val coordinates: Coordinates,
                      val transactions: List<String>,
                      val price: String,
                      val location: Location,
                      val phone: String,
                      val displat_phone: String,
                      val distance: Double)


data class Category(val alias: String, val title: String)
data class Coordinates(val latitude: Double, val longitude: Double)
data class Location(val address1: String,
                    val address2: String,
                    val address3: String,
                    val city: String,
                    val zip_code: String,
                    val country: String,
                    val state: String,
                    val display_address: List<String>)