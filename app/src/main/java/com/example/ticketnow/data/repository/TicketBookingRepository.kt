package com.example.ticketnow.data.repository

import android.content.Context
import android.database.DatabaseUtils
import android.util.Log
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.utils.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TicketBookingRepository(context: Context) {
    private val helper = DatabaseHelper(context)
    private val TAG = "BOOK_MY_MOVIE"

    fun insert(movieId: Int, theatreId: Int, userId: Int, ticketCount: Int) = CoroutineScope(
        Dispatchers.Main).launch {
        try {
            helper.insertBooking(movieId, theatreId, userId, ticketCount)
            Log.d(TAG, "Data inserted successfully")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getData(): List<BookTicketModel> {

        val list = mutableListOf<BookTicketModel>()
        val cursor = helper.getAllBookings

        if (cursor.moveToFirst()) {
            Log.d("BOOKING_TAG", DatabaseUtils.dumpCursorToString(cursor))
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
}