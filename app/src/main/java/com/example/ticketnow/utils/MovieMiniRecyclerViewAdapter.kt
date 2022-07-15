package com.example.ticketnow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import kotlinx.android.synthetic.main.card_layout_movie.view.*
import kotlinx.android.synthetic.main.card_layout_theatre.view.*

class MovieMiniRecyclerViewAdapter(
    private val theatres: List<MovieModel>,
    val btnClickListener: BtnClickListener
    ) : RecyclerView.Adapter<MovieMiniRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieMiniRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieMiniRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.apply {
            itemTitle.text = theatres[position].name
            itemDesc.text = theatres[position].genre
            itemImage.setImageResource(R.drawable.minion)
        }
    }

    override fun getItemCount(): Int = theatres.size.coerceAtMost(4)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView
        val itemTitle: TextView
        val itemDesc: TextView

        init {
            itemImage = view.item_movie_image
            itemTitle = view.item_movie_title
            itemDesc = view.item_movie_desc

            view.setOnClickListener {
                btnClickListener.clickListener(this.layoutPosition)
            }
        }
    }
}