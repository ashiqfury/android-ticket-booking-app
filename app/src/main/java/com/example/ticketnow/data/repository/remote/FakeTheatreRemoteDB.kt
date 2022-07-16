package com.example.ticketnow.data.repository.remote

import android.content.Context
import android.util.Log
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.repository.MovieRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random.Default.nextInt

object FakeTheatreRemoteDB : TheatreRemoteInterface {

    override suspend fun getAllTheatres(context: Context): List<TheatreModel> {
        val gson = Gson()

        val theatreType = object : TypeToken<List<TheatreModel>>() {}.type
        val theatresJsonString: String =
            context.assets.open("theatresData.json").bufferedReader().use { it.readText() }

        val theatreList =  gson.fromJson(theatresJsonString, theatreType) as List<TheatreModel>

        val movieRepository = MovieRepository(context)
        val movies = movieRepository.getAllData()

        theatreList.forEach {
            it.moviesList = getMoviesIdList(movies).toSet().toList()
        }

        return theatreList
    }

    private fun getMoviesIdList(movies: List<MovieModel>): List<MovieModel> {
        return List(4) { movies[nextInt(0, movies.size - 1)] }
    }
}