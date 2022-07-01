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
import kotlinx.android.synthetic.main.card_layout.view.*

internal class TheatreRecyclerViewAdapter(private val movies: List<TheatreModel>, val btnClickListener: BtnClickListener) : RecyclerView.Adapter<TheatreRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            itemTitle.text = movies[position].name
            itemDesc.text = movies[position].location
            itemImage.setImageResource(R.drawable.theatres)
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
                btnClickListener.clickListener(this.layoutPosition)
            }
        }

    }
}