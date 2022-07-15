package com.example.ticketnow.data.repository.remote

import android.content.Context
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel

interface TheatreRemoteInterface {
    suspend fun getAllTheatres(context: Context): List<TheatreModel>
}