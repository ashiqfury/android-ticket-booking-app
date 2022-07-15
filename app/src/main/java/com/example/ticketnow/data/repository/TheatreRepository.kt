package com.example.ticketnow.data.repository

import android.content.Context
import android.database.DatabaseUtils
import android.util.Log
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.repository.remote.FakeTheatreRemoteDB
import com.example.ticketnow.utils.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TheatreRepository(val context: Context) {
    private val helper = DatabaseHelper(context)

    suspend fun insert(name: String, location: String, totalSeats: Int, availableSeats: Int, stared: Int = 0) {
        helper.insertTheatre(name, location, totalSeats, availableSeats, stared)
    }

    private suspend fun getTheatresFromDB(): List<TheatreModel> {
        val list = mutableListOf<TheatreModel>()
        val cursor = helper.getAllTheatres()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id: String = cursor.getString(cursor.getColumnIndex("id"))
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val location: String = cursor.getString(cursor.getColumnIndex("location"))
                val totalSeats: String = cursor.getString(cursor.getColumnIndex("totalSeats"))
                val availableSeats: String = cursor.getString(cursor.getColumnIndex("availableSeats"))
                val stared: String = cursor.getString(cursor.getColumnIndex("stared"))
                val theatre = TheatreModel(id.toInt(), name, location, totalSeats.toInt(), availableSeats.toInt(), stared.toInt())
                list.add(theatre)
                cursor.moveToNext()
            }
        }
        return list
    }

    private suspend fun getTheatresFromNetwork(): List<TheatreModel> {
        return FakeTheatreRemoteDB.getAllTheatres(context)
    }

    suspend fun getData(): List<TheatreModel> {
        val cursor = helper.getAllTheatres()
        val theatres: List<TheatreModel> = if (cursor.count > 0) {
            getTheatresFromDB()
        } else {
            val theatres = getTheatresFromNetwork()
            helper.deleteAllMovies()
            theatres.forEach { theatre -> insert(theatre.name, theatre.location, theatre.totalSeats, theatre.availableSeats, theatre.stared) }
            theatres
        }
        return theatres
    }

    suspend fun getTheatre(theatreId: Int): TheatreModel {
        var theatre: TheatreModel? = null
        val cursor = helper.getTheatre(theatreId)
        cursor.moveToFirst()

        if (cursor.moveToFirst()) {
            val id: String = cursor.getString(cursor.getColumnIndex("id"))
            val name: String = cursor.getString(cursor.getColumnIndex("name"))
            val location: String = cursor.getString(cursor.getColumnIndex("location"))
            val totalSeats: String = cursor.getString(cursor.getColumnIndex("totalSeats"))
            val availableSeats: String = cursor.getString(cursor.getColumnIndex("availableSeats"))
            val stared: String = cursor.getString(cursor.getColumnIndex("stared"))
            theatre = TheatreModel(id.toInt(), name, location, totalSeats.toInt(), availableSeats.toInt(), stared.toInt())
            cursor.moveToNext()
        }
        return theatre!!
    }

    suspend fun deleteTheatre(theatreId: Int) {
        helper.deleteTheatre(theatreId.toString())
    }

    suspend fun updateStar(id: Int, value: Int) {
        helper.updateStar(id.toString(), value)
    }

    suspend fun getStar(theatreId: Int): Int {
        val cursor = helper.getTheatre(theatreId)
        cursor.moveToFirst()
        var stared = 0

        if (cursor.moveToFirst()) {
            stared = cursor.getInt(cursor.getColumnIndex("stared"))
            cursor.moveToNext()
        }
        return stared
    }
}