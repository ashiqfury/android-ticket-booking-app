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
import kotlinx.android.synthetic.main.movie_item_view_pager.view.*
import kotlinx.android.synthetic.main.theatre_item_view_pager.view.*

class MovieViewPagerAdapter(
    private val movies: List<MovieModel>,
    private val btnClickListener: BtnClickListener
): RecyclerView.Adapter<MovieViewPagerAdapter.ViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item_view_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val movie = movies[position]
        holder.apply {
            itemImage.setImageResource(R.drawable.minion)
            itemTitle.text = movie.name
            itemDesc.text = "This is the description of this theatre. It one of the popular theatre."
            itemLanguage.text = "Language: ${movie.language}"
            itemGenre.text = "Total seats: ${movie.genre}"
            itemTime.text = "Show time: ${movie.time}"
            itemPrice.text = "Price: ${movie.price}"
        }
    }

    override fun getItemCount(): Int  = movies.size

    inner class ViewPagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView
        val itemTitle: TextView
        val itemDesc: TextView
        val itemLanguage: TextView
        val itemGenre: TextView
        val itemTime: TextView
        val itemPrice: TextView

        init {
            itemImage = view.findViewById(R.id.movie_image)
            itemTitle = view.findViewById(R.id.movie_title)
            itemDesc = view.movie_desc
            itemLanguage = view.movie_language
            itemGenre = view.movie_genre
            itemTime = view.movie_showtime
            itemPrice = view.movie_price

           /* val btn = view.findViewById<Button>(R.id.btn_book_ticket)
            btn.setOnClickListener {
                btnClickListener.clickListener(this.layoutPosition)
            }*/
        }
    }
}