package com.example.ticketnow.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.data.repository.MovieRepository
import com.example.ticketnow.data.repository.TheatreRepository
import com.example.ticketnow.data.repository.TicketBookingRepository
import com.example.ticketnow.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BookingDetailViewModel: ViewModel() {
//    private lateinit var movieRepository: MovieRepository
//    private lateinit var theatreRepository: TheatreRepository
    private lateinit var bookingRepository: TicketBookingRepository
    private lateinit var userRepository: UserRepository
    private lateinit var bookings: List<BookTicketModel>

    fun initializeRepo(context: Context) {
//        movieRepository = MovieRepository(context)
//        theatreRepository = TheatreRepository(context)
        bookingRepository = TicketBookingRepository(context)
        userRepository = UserRepository(context)
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            bookings = bookingRepository.getData()
        }
    }

    fun insertBooking(movieId: Int, theatreId: Int, userId: Int, ticketCount: Int): Long {
        var id: Long = -1
         viewModelScope.launch {
            id =  bookingRepository.insert(movieId, theatreId, userId, ticketCount)
        }
        return id
    }

    fun insertUser(name: String, number: Long): Long {
        var id: Long = -1
        viewModelScope.launch {
            id = userRepository.insert(name, number)
        }
        return id
    }
}