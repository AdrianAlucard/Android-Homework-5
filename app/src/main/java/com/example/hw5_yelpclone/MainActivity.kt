package com.example.hw5_yelpclone

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val BASE_URL = "https://api.yelp.com/v3/"
    private val businessList: ArrayList<YelpReview> = ArrayList()
    private lateinit var yelpApi: YelpService

    private val token =
        "<Insert Token Here>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = YelpRecyclerAdapter(businessList)
        yelp_recycler_view.adapter = adapter
        yelp_recycler_view.layoutManager = LinearLayoutManager(this)

        val client = OkHttpClient.Builder().addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response? {
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                return chain.proceed(newRequest)
            }
        }).build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        yelpApi = retrofit.create(YelpService::class.java)
    }

    fun searchButton(view: View) {
        val foodItem = food_search.text.toString()
        val locationSearch = location_search.text.toString()

        if(isInputValid(foodItem, locationSearch)) {
            yelpApi.SearchYelp(foodItem, locationSearch)
                .enqueue(object : retrofit2.Callback<YelpResults> {
                    override fun onFailure(call: Call<YelpResults>, t: Throwable) {
                        Log.d(TAG, "onFailure : $t")
                    }

                    override fun onResponse(
                        call: Call<YelpResults>,
                        response: retrofit2.Response<YelpResults>
                    ) {
                        val body = response.body()
                        Log.d(TAG, body.toString())

                        if (body == null || body.businesses.isNullOrEmpty()) {
                            Log.w(TAG, "Valid response was not received")
                            Toast.makeText(this@MainActivity, "No Results Found", Toast.LENGTH_SHORT).show()
                            return
                        }
                        Log.d(TAG, ": ${body.businesses?.get(0).name}")
                        Log.d(TAG, ": ${body.businesses?.get(0).image_url}")
                        Log.d(TAG, ": ${body.businesses?.get(0).review_count}")
                        Log.d(TAG, ": ${body.businesses?.get(0).rating}")
                        Log.d(TAG, ": ${body.businesses?.get(0).location.display_address}")
                        businessList.clear() // remove old search results
                        businessList.addAll(body.businesses);
                        (yelp_recycler_view.adapter as YelpRecyclerAdapter).notifyDataSetChanged()
                    }

                })
            view.hideKeyboard();
        }
    }

    /**
     * when input invalid, show popup modal to inform user of bad input
     */
    private fun isInputValid(foodItem: String, locationSearch: String): Boolean {
        val builder = AlertDialog.Builder(this)
        var title = "Missing Search Term"
        var message: String

        if(foodItem.isNullOrBlank()) {
            message = "Please enter search term in food input field"
        } else if(locationSearch.isNullOrBlank()) {
            message = "Please enter location in location input field"
        } else {
            return true
        }

        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.ic_delete)
        builder.setNeutralButton("Dismiss") {dialog, which ->
            dialog.dismiss()
        }

        builder.create().show()
        return false
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
