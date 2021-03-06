package com.example.ticketnow.data.repository.remote

import android.content.Context
import com.example.ticketnow.data.models.MovieModel

interface MovieRemoteInterface {
    suspend fun getAllMovies(context: Context): List<MovieModel>
}