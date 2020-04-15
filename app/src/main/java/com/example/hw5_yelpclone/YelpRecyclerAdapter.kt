package com.example.hw5_yelpclone

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_item.view.*
import java.util.ArrayList

class YelpRecyclerAdapter(private val reviews: ArrayList<YelpReview>) : RecyclerView.Adapter<YelpRecyclerAdapter.MyViewHolder>() {
    private val TAG = "YelpRecyclerAdapter"

    // inflate layout from row_item.xml and return the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       Log.d(TAG, "onCreateViewHolder")
       val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
       return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentYelpReview = reviews[position]
        holder.businessName.text = currentYelpReview.businessName
        holder.reviewCount.text = currentYelpReview.reviewCount.toString()
        holder.ratingBar.rating = currentYelpReview.rating
        holder.distance.text = currentYelpReview.distance
        holder.address.text = currentYelpReview.address
        holder.category.text = currentYelpReview.category
        // use Picasso to load image url
        holder.image.setImageResource(R.drawable.ic_launcher_foreground)
        holder.price.text  = currentYelpReview.price
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    // provides reference to the views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val businessName = itemView.business_name
        val reviewCount = itemView.num_reviews
        val ratingBar = itemView.ratingBar
        val distance = itemView.distance
        val address = itemView.address
        val category = itemView.category
        val image = itemView.business_image
        val price = itemView.price
    }
}