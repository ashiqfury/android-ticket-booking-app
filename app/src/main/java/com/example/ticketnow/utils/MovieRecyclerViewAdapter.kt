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
import kotlinx.android.synthetic.main.card_layout_movie_list.view.*

internal class MovieRecyclerViewAdapter(private val movies: List<MovieModel>, val btnClickListener: RecyclerViewClickListener) : RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_movie_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            itemTitle.text = movies[position].name
            itemDesc.text = movies[position].genre
            itemImage.setImageResource(R.drawable.minion)
        }
    }

    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView
        val itemTitle: TextView
        val itemDesc: TextView

        init {
            itemImage = view.item_image
            itemTitle = view.item_title
            itemDesc = view.item_desc

            view.setOnClickListener {
                val position = this.layoutPosition
                val movieId = movies[position].id
                btnClickListener.clickListener(movieId, false)
            }

            view.findViewById<Button>(R.id.btn_movie_card_list).setOnClickListener {
                val position = this.layoutPosition
                val movieId = movies[position].id
                btnClickListener.clickListener(movieId, true)
            }
        }

    }
}