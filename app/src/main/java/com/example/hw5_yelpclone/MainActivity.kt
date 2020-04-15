package com.example.hw5_yelpclone

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import java.io.IOException
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val BASE_URL = "https://api.yelp.com/v3/"
    private val token =
        "<insert API token here>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val businessList = ArrayList<YelpReview>()
        val adapter = YelpRecyclerAdapter(businessList)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val yelpApi = retrofit.create(YelpService::class.java)

        yelp_recycler_view.adapter = adapter

        yelp_recycler_view.layoutManager = LinearLayoutManager(this)

        val results = yelpApi.SearchYelp("pizza", "New Britain", "Bearer $token")
            .enqueue(object : retrofit2.Callback<YelpResults> {
                override fun onFailure(call: Call<YelpResults>, t: Throwable) {
                    Log.d(TAG, "onFailure : $t")
                }

                override fun onResponse(
                    call: Call<YelpResults>,
                    response: retrofit2.Response<YelpResults>
                ) {
                    val body = response.body()

                    if (body == null) {
                        Log.w(TAG, "Valid response was not received")
                        return
                    }
                    Log.d(TAG, ": ${body.businesses?.get(0).name}")
                    Log.d(TAG, ": ${body.businesses?.get(0).image_url}")
                    Log.d(TAG, ": ${body.businesses?.get(0).review_count}")
                    Log.d(TAG, ": ${body.businesses?.get(0).rating}")
                    Log.d(TAG, ": ${body.businesses?.get(0).location.display_address}")

                    businessList.addAll(body.businesses);
                    adapter.notifyDataSetChanged()
                }

            })
    }
}
