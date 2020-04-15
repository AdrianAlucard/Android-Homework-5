package com.example.hw5_yelpclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        yelp_recycler_view.adapter = YelpRecyclerAdapter(stubbedData())

        yelp_recycler_view.layoutManager = LinearLayoutManager(this)
    }

    private fun stubbedData() : ArrayList<YelpReview> {
        val scrubbyDubbyScrubLords = ArrayList<YelpReview>()
        for(i in 0..10) {
            scrubbyDubbyScrubLords.add(YelpReview("Long John Silver's $i", 75 + 3*i, 4f,
                            "${i+2} mi", "1616 Stanley Street New Britain CT Planet Earth", "Fast Food",
                                    "nonethingHereYet", "$$"))
        }
        return scrubbyDubbyScrubLords
    }
}
