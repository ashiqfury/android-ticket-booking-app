package com.example.ticketnow.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.data.repository.MovieRepository
import com.example.ticketnow.data.repository.TheatreRepository
import com.example.ticketnow.data.repository.TicketBookingRepository
import com.example.ticketnow.data.repository.UserRepository
import kotlinx.coroutines.channels.ticker

class BookingConfirmViewModel : ViewModel() {

    private lateinit var bookingRepository: TicketBookingRepository
    private lateinit var userRepository: UserRepository
    private lateinit var movieRepository: MovieRepository
    private lateinit var theatreRepository: TheatreRepository

    fun initializeRepo(context: Context) {
        bookingRepository = TicketBookingRepository(context)
        userRepository = UserRepository(context)
        movieRepository = MovieRepository(context)
        theatreRepository = TheatreRepository(context)
    }

    fun getBooking(bookingId: Int): BookTicketModel {
        return bookingRepository.getBooking(bookingId)
    }

    fun getUser(userId: Int): UserModel {
        return userRepository.getUser(userId)
    }

    fun getMovie(movieId: Int): MovieModel {
        return movieRepository.getMovie(movieId)
    }

    fun getTheatre(theatreId: Int): TheatreModel {
        return theatreRepository.getTheatre(theatreId)
    }

}