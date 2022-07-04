package com.example.ticketnow.data.repository

import android.content.Context
import android.database.DatabaseUtils
import android.util.Log
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.utils.DatabaseHelper

class TicketBookingRepository(context: Context) {
    private val helper = DatabaseHelper(context)
    private val TAG = "BOOK_MY_MOVIE"

    fun insert(movieId: Int, theatreId: Int, userId: Int, ticketCount: Int): Long {
        return try {
            Log.d(TAG, "Data inserted successfully")
            helper.insertBooking(movieId, theatreId, userId, ticketCount)
        } catch (e: Exception){
            e.printStackTrace()
            -1
        }

    }

    fun getData(): List<BookTicketModel> {

        val list = mutableListOf<BookTicketModel>()
        val cursor = helper.getAllBookings

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id: String = cursor.getString(cursor.getColumnIndex("id"))
                val movieId: String = cursor.getString(cursor.getColumnIndex("movieId"))
                val theatreId: String = cursor.getString(cursor.getColumnIndex("theatreId"))
                val userId: String = cursor.getString(cursor.getColumnIndex("userId"))
                val ticketCount: String = cursor.getString(cursor.getColumnIndex("ticketCount"))
                val book = BookTicketModel(id.toInt(), movieId.toInt(), theatreId.toInt(), userId.toInt(), ticketCount.toInt())
                list.add(book)
                cursor.moveToNext()
                Log.d(TAG, book.toString())
            }
        }
        return list
    }

    fun getBooking(bookingId: Int): BookTicketModel {
        var book: BookTicketModel? = null
        val cursor = helper.getBooking(bookingId)
        cursor.moveToFirst()

        if (cursor.moveToFirst()) {
            val id: String = cursor.getString(cursor.getColumnIndex("id"))
            val movieId: String = cursor.getString(cursor.getColumnIndex("movieId"))
            val theatreId: String = cursor.getString(cursor.getColumnIndex("theatreId"))
            val userId: String = cursor.getString(cursor.getColumnIndex("userId"))
            val ticketCount: String = cursor.getString(cursor.getColumnIndex("ticketCount"))
            book = BookTicketModel(
                id.toInt(),
                movieId.toInt(),
                theatreId.toInt(),
                userId.toInt(),
                ticketCount.toInt()
            )
            cursor.moveToNext()
        }
        return book!!
    }
}