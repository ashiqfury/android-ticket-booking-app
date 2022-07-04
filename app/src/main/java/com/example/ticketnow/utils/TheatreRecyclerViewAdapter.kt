package com.example.ticketnow.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.ui.TAG
import kotlinx.android.synthetic.main.card_layout_theatre_list.view.*

internal class TheatreRecyclerViewAdapter(private val theatres: List<TheatreModel>, val btnClickListener: StarBtnClickListener) : RecyclerView.Adapter<TheatreRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_theatre_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            itemTitle.text = theatres[position].name
            itemDesc.text = theatres[position].location
            Log.d(TAG, "onBindViewHolder: ${theatres[position]}")

            if (theatres[position].stared == 1) {
                itemStar.setImageResource(R.drawable.ic_star_filled)
            } else {
                itemStar.setImageResource(R.drawable.ic_star_border)
            }
            itemImage.setImageResource(R.drawable.theatres)
        }
        Log.d(TAG, "onBindViewHolder: is called")
    }

    override fun getItemCount(): Int = theatres.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView
        val itemTitle: TextView
        val itemDesc: TextView
        val itemStar: ImageButton

        init {
            itemImage = view.item_image
            itemTitle = view.item_title
            itemDesc = view.item_desc
            itemStar = view.btn_theatre_list

            view.setOnClickListener {
                val position = this.layoutPosition
                val theatreId = theatres[position].id
                btnClickListener.clickListener(theatreId, false)
            }

            var bool = false
            itemStar.setOnClickListener {
//                bool = if (bool) {
//                    itemStar.setImageResource(R.drawable.ic_star_border)
//                    !bool
//                } else {
//                    itemStar.setImageResource(R.drawable.ic_star_filled)
//                    !bool
//                }
                val position = this.layoutPosition
                val theatreId = theatres[position].id
                btnClickListener.clickListener(theatreId, true)
            }
        }

    }
}