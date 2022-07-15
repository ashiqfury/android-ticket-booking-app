package com.example.ticketnow.data.repository.remote

import android.content.Context
import com.example.ticketnow.data.models.MovieModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FakeMovieRemoteDB : MovieRemoteInterface {

    override suspend fun getAllMovies(context: Context): List<MovieModel> {
        val gson = Gson()

        val movieType = object : TypeToken<List<MovieModel>>() {}.type
        val moviesJsonString: String =
            context.assets.open("moviesdata.json").bufferedReader().use { it.readText() }

        return gson.fromJson(moviesJsonString, movieType);
    }
}