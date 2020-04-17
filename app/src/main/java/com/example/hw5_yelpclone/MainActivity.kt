package com.example.hw5_yelpclone

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val BASE_URL = "https://api.yelp.com/v3/"
    private val alertMessageTitle: String = "Missing Search Term"
    private val foodSearchAlertMessage: String = "Please enter search term in food input field"
    private val locationSearchAlertMessage: String = "Please enter location in location input field"
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
                .enqueue(yelpCallback())
            view.hideKeyboard();
        }
    }

    fun nearMeButton(view: View) {
        val foodItem = food_search.text.toString()
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(isQueryStringEmpty(foodItem)) {
            showDialog(alertMessageTitle, foodSearchAlertMessage)
        } else {
            //check app permissions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                ActivityCompat.requestPermissions(this,permissions, 0)
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(location != null) {
                    Log.d(TAG, "lat: ${location.latitude} long: ${location.longitude}")
                    // radius about 10 miles
                    yelpApi.SearchYelp(foodItem, location.latitude, location.longitude,16093)
                        .enqueue(yelpCallback())
                } else {
                    showDialog("GPS Issue", "Unable to fetch your location for this feature")
                }
                view.hideKeyboard();
            }
        }
    }

    /**
     * returns reusable callback method to handle yelpApi response
     */
    private fun yelpCallback(): Callback<YelpResults> {
        return object : Callback<YelpResults> {
            override fun onFailure(call: Call<YelpResults>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
            }

            override fun onResponse(
                call: Call<YelpResults>,
                response: retrofit2.Response<YelpResults>
            ) {
                handleYelpResponse(response)
            }

        }
    }

    /**
     * Updates UI with Yelp data
     * @param retrofit2.Response<YelpResults>
     */
    private fun handleYelpResponse(response: retrofit2.Response<YelpResults>) {
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

    /**
     * when input invalid, show popup modal to inform user of bad input
     */
    private fun isInputValid(foodItem: String, locationSearch: String): Boolean {
        if(isQueryStringEmpty(foodItem)) {
            showDialog(alertMessageTitle, foodSearchAlertMessage)
        } else if(isQueryStringEmpty(locationSearch)) {
            showDialog(alertMessageTitle, locationSearchAlertMessage)
        } else {
            return true
        }
        return false
    }

    private fun isQueryStringEmpty(query: String): Boolean {
        return query.isNullOrEmpty()
    }

    private fun showDialog(title: String ,message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.ic_delete)
        builder.setNeutralButton("Dismiss") {dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
