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

    fun insert(name: String, location: String, totalSeats: Int, availableSeats: Int) = CoroutineScope(
        Dispatchers.Main).launch {
        try {
            helper.insertTheatre(name, location, totalSeats, availableSeats)
            Log.d(TAG, "Data inserted successfully")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    fun getData(): List<TheatreModel> {

        val list = mutableListOf<TheatreModel>()
        val cursor = helper.getAllTheatres

        if (cursor.moveToFirst()) {
            Log.d("BOOKING_TAG", DatabaseUtils.dumpCursorToString(cursor))
            while (!cursor.isAfterLast) {
                val id: String = cursor.getString(cursor.getColumnIndex("id"))
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val location: String = cursor.getString(cursor.getColumnIndex("location"))
                val totalSeats: String = cursor.getString(cursor.getColumnIndex("totalSeats"))
                val availableSeats: String = cursor.getString(cursor.getColumnIndex("availableSeats"))
                val theatre = TheatreModel(id.toInt(), name, location, totalSeats.toInt(), availableSeats.toInt())
                Log.d("BOOK", theatre.toString())
                list.add(theatre)
                cursor.moveToNext()
            }
        }
        return list
    }
}