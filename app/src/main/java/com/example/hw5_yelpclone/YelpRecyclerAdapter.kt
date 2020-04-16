package com.example.hw5_yelpclone

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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
        holder.businessName.text = currentYelpReview.name
        holder.reviewCount.text = currentYelpReview.review_count.toString()
        holder.ratingBar.rating = currentYelpReview.rating.toFloat()
        holder.distance.text = formattedDistance(currentYelpReview.distance)
        holder.address.text = "${currentYelpReview.location.display_address[0]}, ${currentYelpReview.location.display_address[1]}"
        holder.category.text = currentYelpReview.categories[0].title

        if(!currentYelpReview.image_url.isNullOrEmpty()) {
            Picasso.get().load(currentYelpReview.image_url).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_foreground)  //image place holder
        }

        holder.price.text  = currentYelpReview.price
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    private fun formattedDistance(distance: Double): String {
        val distance = convertToMiles(distance)
        return "${String.format("%.2f", distance)} mi"
    }

    private fun convertToMiles(distance: Double): Double {
        return distance / 1609.344 // magic number. yay conversions!
    }

    // provides reference to the views
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val businessName = itemView.business_name
        val reviewCount = itemView.num_reviews
        val ratingBar = itemView.ratingBar
        val distance = itemView.distance
        val address = itemView.address
        val category = itemView.category
        val image = itemView.business_image
        val price = itemView.price

        init {
            itemView.setOnClickListener{
                val selectedItem = reviews[adapterPosition]
                val address = selectedItem.location.display_address[0] + "," + selectedItem.location.display_address[1]

                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=$address"))
                itemView.context.startActivity(intent)
             }
        }
    }
}