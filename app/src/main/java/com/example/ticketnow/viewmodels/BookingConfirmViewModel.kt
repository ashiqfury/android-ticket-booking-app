package com.example.ticketnow.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.data.repository.MovieRepository
import com.example.ticketnow.data.repository.TheatreRepository
import com.example.ticketnow.data.repository.TicketBookingRepository
import com.example.ticketnow.data.repository.UserRepository

class BookingConfirmViewModel : ViewModel() {

    private lateinit var movieRepository: MovieRepository
    private lateinit var theatreRepository: TheatreRepository
    private lateinit var bookingRepository: TicketBookingRepository
    private lateinit var userRepository: UserRepository
    private lateinit var bookings: List<BookTicketModel>

    fun initializeRepo(context: Context) {
        movieRepository = MovieRepository(context)
        theatreRepository = TheatreRepository(context)
        bookingRepository = TicketBookingRepository(context).also {
            bookings = it.getData()
        }
        userRepository = UserRepository(context)
    }

    fun getMovieFromBooking(bookingId: Int): MovieModel? {
        val booking = bookings.find { it.id == bookingId }
        return movieRepository.getData().find { it.id == booking?.movieId }
    }

    fun getTheatreFromBooking(bookingId: Int): TheatreModel? {
        val booking = bookings.find { it.id == bookingId }
        return theatreRepository.getData().find { it.id == booking?.theatreId }
    }

    fun getUserFromBooking(bookingId: Int): UserModel? {
        val booking = bookings.find { it.id == bookingId }
        return userRepository.getData().find { it.id == booking?.userId }
    }

    fun getTicketCount(bookingId: Int): Int? {
        return bookings.find { it.id == bookingId }?.ticketCount
    }
}