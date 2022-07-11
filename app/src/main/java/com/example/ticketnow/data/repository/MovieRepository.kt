package com.example.ticketnow.data.repository

import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.util.Log
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.repository.remote.FakeMovieRemoteDB
import com.example.ticketnow.utils.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MovieRepository(val context: Context) {
    private val helper = DatabaseHelper(context)

    private suspend fun getMoviesFromNetwork(): List<MovieModel> {
        delay(2000) // simulated delay
        return FakeMovieRemoteDB.getAllMovies(context)
    }

    private suspend fun getMoviesFromDB(): List<MovieModel> {
        val list = mutableListOf<MovieModel>()
        val cursor = helper.getAllMovies()

        cursor.apply {
            if (moveToFirst()) {
                while (!cursor.isAfterLast) {
                    MovieModel(
                        getValueFromCursor(this,"id").toInt(),
                        getValueFromCursor(this,"name"),
                        getValueFromCursor(this,"genre"),
                        getValueFromCursor(this,"language"),
                        getValueFromCursor(this,"showtime"),
                        getValueFromCursor(this, "price").toInt(),
                    ).also { list.add(it) }
                    moveToNext()
                }
            }
        }

        return list
    }

    suspend fun getAllData(): List<MovieModel> {
        val cursor = helper.getAllMovies()
        return if (cursor.count > 0) {
            getMoviesFromDB()
        }
        else {
            val movies = getMoviesFromNetwork()
            helper.deleteAllMovies()
            movies.forEach { movie -> insert(movie.name, movie.genre, movie.language, movie.time, movie.price) }
            movies
        }
    }

    suspend fun getData(page: Int, limit: Int): List<MovieModel> {
        val movies = getAllData()
        return movies.subList((page - 1) * limit, page * limit)
    }

    suspend fun getUpdatedMovies(): List<MovieModel> {
        return if (getMoviesFromNetwork().size != getMoviesFromDB().size) {
            val movies = getMoviesFromNetwork()
            helper.deleteAllMovies()
            movies.forEach { movie -> insert(movie.name, movie.genre, movie.language, movie.time, movie.price) }
            movies
        } else {
            getMoviesFromDB()
        }
    }

    suspend fun insert(name: String, genre: String, language: String, showTime: String, price: Int) {
        try {
            helper.insertMovie(name, genre, language, showTime, price)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    suspend fun getMovie(movieId: Int): MovieModel {
        var movie: MovieModel? = null
        val cursor = helper.getMovie(movieId)

        cursor.apply {
            if (moveToFirst()) {
                movie = MovieModel(
                    getValueFromCursor(this,"id").toInt(),
                    getValueFromCursor(this,"name"),
                    getValueFromCursor(this,"genre"),
                    getValueFromCursor(this,"language"),
                    getValueFromCursor(this,"showtime"),
                    getValueFromCursor(this, "price").toInt(),
                )
                moveToNext()
            }
        }
        return movie!!
    }

    private fun getValueFromCursor(cursor: Cursor, key: String): String {
        return cursor.getString(cursor.getColumnIndex(key))
    }

    /*fun update(id: String, name: String, genre: String, language: String, showTime: String, price: Int) = CoroutineScope(Dispatchers.Main).launch {
        try {
            val isUpdate = helper.updateData(id, name, genre, language, showTime, price)
            if (isUpdate) Log.d(TAG, "Data updated successfully")
            else Log.d(TAG, "Data not updated")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun delete(id: String) = CoroutineScope(Dispatchers.Main).launch {
        try {
            helper.deleteData(id)
            Log.d(TAG, "Data deleted successfully")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }*/
}