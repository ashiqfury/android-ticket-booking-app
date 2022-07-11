package com.example.ticketnow.data.repository

import android.content.Context
import android.database.DatabaseUtils
import android.util.Log
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.utils.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TheatreRepository(context: Context) {
    private val helper = DatabaseHelper(context)
    private val TAG = "BOOK_MY_THEATRE"

    suspend fun insert(name: String, location: String, totalSeats: Int, availableSeats: Int) {
        helper.insertTheatre(name, location, totalSeats, availableSeats)
    }


    suspend fun getData(): List<TheatreModel> {
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
                Log.d(TAG, "Get data is called with: $theatre")
                list.add(theatre)
                cursor.moveToNext()
            }
        }
        return list
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