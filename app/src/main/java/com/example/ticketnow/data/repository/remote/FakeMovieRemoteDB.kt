package com.example.ticketnow.data.repository.remote

import android.content.Context
import android.util.Log
import com.example.ticketnow.data.models.MovieModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException

object FakeMovieRemoteDB {

    suspend fun getAllMovies(context: Context): List<MovieModel> {
        val gson = Gson()

        val movieType = object : TypeToken<List<MovieModel>>() {}.type
        val moviesJsonString: String = context.assets.open("moviesdata.json").bufferedReader().use { it.readText() }

        return gson.fromJson(moviesJsonString, movieType);
    }
}