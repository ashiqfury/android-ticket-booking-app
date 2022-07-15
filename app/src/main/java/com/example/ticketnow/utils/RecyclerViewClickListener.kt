package com.example.ticketnow.utils

interface RecyclerViewClickListener {
    fun clickListener(movieId: Int, position: Int, isButton: Boolean)
}