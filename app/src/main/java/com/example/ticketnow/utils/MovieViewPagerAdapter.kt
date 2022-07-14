package com.example.ticketnow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import kotlinx.android.synthetic.main.item_view_view_pager.view.*

class MovieViewPagerAdapter(
    private val movies: List<MovieModel>,
    private val btnClickListener: BtnClickListener
): RecyclerView.Adapter<MovieViewPagerAdapter.ViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_view_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val movie = movies[position]
        holder.apply {
            itemImage.setImageResource(R.drawable.minion)
//            itemTitle.text = movie.name
//            itemDesc.text = "This is the description of this theatre. It one of the popular theatre."
//            itemLocation.text = "Location: ${movie.location}"
//            itemTotalSeats.text = "Total seats: ${movie.totalSeats}"
//            itemAvailableSeats.text = "Available seats: ${movie.availableSeats}"
        }

    }

    override fun getItemCount(): Int  = movies.size

    inner class ViewPagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView
        val itemTitle: TextView
        val itemDesc: TextView
        val itemLocation: TextView
        val itemTotalSeats: TextView
        val itemAvailableSeats: TextView

        init {
            itemImage = view.theatre_image
            itemTitle = view.theatre_title
            itemDesc = view.theatre_desc
            itemLocation = view.theatre_location
            itemTotalSeats = view.theatre_total_seats
            itemAvailableSeats = view.theatre_available_seats

            val btn = view.findViewById<Button>(R.id.btn_book_ticket)
            btn.setOnClickListener {
                btnClickListener.clickListener(this.layoutPosition)
            }
        }

    }
}