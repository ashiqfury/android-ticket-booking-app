package com.example.ticketnow.utils

import android.database.Cursor
import com.example.ticketnow.data.models.MovieModel

interface DatabaseInterface {
    suspend fun insertMovie(name: String, genre: String, language: String, showtime: String, price: Int)
    suspend fun insertBooking(movieId: Int, theatreId: Int, userId: Int, ticketCount: Int): Long
    suspend fun insertTheatre(name: String, location: String, totalSeats: Int, availableSeats: Int, stared: Int?, moviesList: List<MovieModel>)
    suspend fun insertUser(name: String, phoneNumber: Long): Long

    suspend fun getAllMovies(): Cursor
    suspend fun getAllTheatres(): Cursor
    suspend fun getAllUsers(): Cursor
    suspend fun getAllBookings(): Cursor

    suspend fun getUser(id: Int): Cursor
    suspend fun getMovie(id: Int): Cursor
    suspend fun getBooking(id: Int): Cursor
    suspend fun getTheatre(id: Int): Cursor

    suspend fun getMoviesFromOffset(offset: Int): Cursor

    suspend fun deleteTheatre(theatreId : String) : Int
    suspend fun deleteMovie(movieId: String): Int

    suspend fun updateStar(id: String, value: Int)

    suspend fun deleteAllMovies()
    suspend fun deleteAllTheatres()

    suspend fun updateTheatre(id: String, name: String, location: String, totalSeats: Int, availableSeats: Int): Boolean
}