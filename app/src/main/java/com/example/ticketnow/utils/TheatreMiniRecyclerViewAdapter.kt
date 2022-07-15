package com.example.ticketnow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.TheatreModel
import kotlinx.android.synthetic.main.card_layout_theatre.view.*

class TheatreMiniRecyclerViewAdapter(
    private val theatres: List<TheatreModel>,
    val btnClickListener: BtnClickListener
    ) : RecyclerView.Adapter<TheatreMiniRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheatreMiniRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_theatre, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TheatreMiniRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.apply {
            itemTitle.text = theatres[position].name
            itemDesc.text = theatres[position].location
            itemImage.setImageResource(R.drawable.theatres)
        }
    }

    override fun getItemCount(): Int = theatres.size.coerceAtMost(4)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView
        val itemTitle: TextView
        val itemDesc: TextView

        init {
            itemImage = view.item_theatre_image
            itemTitle = view.item_theatre_title
            itemDesc = view.item_theatre_desc

            view.setOnClickListener {
                btnClickListener.clickListener(this.layoutPosition)
            }
        }
    }
}