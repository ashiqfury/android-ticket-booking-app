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
import kotlinx.coroutines.launch

class MovieRepository(val context: Context) {
    private val helper = DatabaseHelper(context)
    private val TAG = "BOOK_MY_MOVIE"


    fun getMoviesFromNetwork(): List<MovieModel> {
        return FakeMovieRemoteDB.getAllMovies(context)
    }

    fun getMoviesFromDB(): List<MovieModel> {
        val list = mutableListOf<MovieModel>()
        val cursor = helper.getAllMovies

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

    fun insert(name: String, genre: String, language: String, showTime: String, price: Int) = CoroutineScope(Dispatchers.Main).launch {
        try {
            helper.insertMovie(name, genre, language, showTime, price)
            Log.d(TAG, "Data inserted successfully")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getData(): List<MovieModel> {
        val cursor = helper.getAllMovies
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

    fun getMovie(movieId: Int): MovieModel {
        var movie: MovieModel? = null
        val cursor = helper.getMovie(movieId)
        cursor.moveToFirst()

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

//        if (cursor.moveToFirst()) {
//            val id: String = cursor.getString(cursor.getColumnIndex("id"))
//            val name: String = cursor.getString(cursor.getColumnIndex("name"))
//            val genre: String = cursor.getString(cursor.getColumnIndex("genre"))
//            val language: String = cursor.getString(cursor.getColumnIndex("language"))
//            val showTime: String = cursor.getString(cursor.getColumnIndex("showtime"))
//            val price: String = cursor.getString(cursor.getColumnIndex("price"))
//            movie = MovieModel(id.toInt(), name, genre, language, showTime, price.toInt())
//            cursor.moveToNext()
//        }
        return movie!!
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

    fun getValueFromCursor(cursor: Cursor, key: String): String {
        return cursor.getString(cursor.getColumnIndex(key))
    }
}