package com.example.ticketnow.data.models

data class TheatreModel(
    val id: Int,
    val name: String,
    val location: String,
    val totalSeats: Int,
    val availableSeats: Int
)