package com.example.ticketnow.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $MOVIE_TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $GENRE TEXT, $LANGUAGE TEXT, $SHOWTIME TEXT, $PRICE INTEGER)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $THEATRE_TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $LOCATION TEXT, $TOTAL_SEATS INTEGER, $AVAILABLE_SEATS INTEGER)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $BOOKING_TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, $MOVIE_ID INTEGER, $THEATRE_ID INTEGER, $USER_ID INTEGER, $TICKET_COUNT INTEGER)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $USER_TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME INTEGER, $PHONE_NUMBER INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $MOVIE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $THEATRE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $BOOKING_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $USER_TABLE")
    }

    fun insertMovie(name: String, genre: String, language: String, showtime: String, price: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.apply {
            put(NAME, name)
            put(GENRE, genre)
            put(LANGUAGE, language)
            put(SHOWTIME, showtime)
            put(PRICE, price)
        }
        Log.d("CONTENT_VALUES MOVIES", contentValues.toString())

        db.insert(MOVIE_TABLE, null, contentValues)
    }

    fun insertTheatre(name: String, location: String, totalSeats: Int, availableSeats: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.apply {
            put(NAME, name)
            put(LOCATION, location)
            put(TOTAL_SEATS, totalSeats)
            put(AVAILABLE_SEATS, availableSeats)
        }
        Log.d("CONTENT_VALUES THEATRE", contentValues.toString())

        db.insert(THEATRE_TABLE, null, contentValues)
    }

    fun insertBooking(movieId: Int, theatreId: Int, userId: Int, ticketCount: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.apply {
            put(MOVIE_ID, movieId)
            put(THEATRE_ID, theatreId)
            put(USER_ID, userId)
            put(TICKET_COUNT, ticketCount)
        }
        Log.d("CONTENT_VALUES BOOKING", contentValues.toString())

        db.insert(BOOKING_TABLE, null, contentValues)
    }

    fun insertUser(name: String, phoneNumber: Long) {
        Log.d("BOOK_MY_SHOW", "DB CALLING")
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.apply {
            put(NAME, name)
            put(PHONE_NUMBER, phoneNumber)
        }
        Log.d("CONTENT_VALUES USER", contentValues.toString())

        db.insert(USER_TABLE, null, contentValues)
    }

    val getAllMovies : Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("SELECT * FROM $MOVIE_TABLE", null)
        }

    val getAllTheatres : Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("SELECT * FROM $THEATRE_TABLE", null)
        }

    val getAllBookings : Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("SELECT * FROM $BOOKING_TABLE", null)
        }

    val getAllUsers : Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("SELECT * FROM $USER_TABLE", null)
        }

    fun getUserByName(name: String): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $USER_TABLE WHERE $NAME = $name", null)
    }

    companion object {
        const val DATABASE_NAME = "ticketnow.db"
        const val DATABASE_VERSION = 1
        const val MOVIE_TABLE = "movies"
        const val THEATRE_TABLE = "theatres"
        const val BOOKING_TABLE = "bookings"
        const val USER_TABLE = "users"
        const val ID = "id"
        const val NAME = "name"
        const val GENRE = "genre"
        const val LANGUAGE = "language"
        const val SHOWTIME = "showtime"
        const val PRICE = "price"
        const val LOCATION = "location"
        const val TOTAL_SEATS = "totalSeats"
        const val AVAILABLE_SEATS = "availableSeats"
        const val MOVIE_ID = "movieId"
        const val THEATRE_ID = "theatreId"
        const val USER_ID = "userId"
        const val TICKET_COUNT = "ticketCount"
        const val PHONE_NUMBER = "phoneNumber"
    }
}