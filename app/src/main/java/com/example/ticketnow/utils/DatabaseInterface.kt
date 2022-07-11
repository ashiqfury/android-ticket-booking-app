package com.example.ticketnow.utils

import android.database.Cursor

interface DatabaseInterface {
    suspend fun insertMovie(name: String, genre: String, language: String, showtime: String, price: Int)
    suspend fun getAllMovies(): Cursor
}