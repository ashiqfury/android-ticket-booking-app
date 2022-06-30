package com.example.ticketnow.data.models

data class BookTicketModel (
    val id: Int,
    val userId: Int,
    val movieId: Int,
    val theatreId: Int,
    val ticketCount: Int
)