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

class BookingDetailViewModel: ViewModel() {
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

    fun getBookings(): List<BookTicketModel> = bookingRepository.getData()

    fun insertBooking(movieId: Int, theatreId: Int, userId: Int, ticketCount: Int) {
        bookingRepository.insert(movieId, theatreId, userId, ticketCount)
    }

    fun insertUser(name: String, number: Long) {
        Log.d("BOOK_MY_SHOW", "VIEWMODEL CALLING $name $number")
        userRepository.insert(name, number)
    }

    fun getMovies(): List<MovieModel> = movieRepository.getData()

    fun getMovieByIndex(index: Int): MovieModel {
        return movieRepository.getData()[index]
    }

    fun getMovieById(id: Int): MovieModel? {
        return movieRepository.getData().find { it.id == id }
    }

    fun getTheatres(): List<TheatreModel> = theatreRepository.getData()

    fun getTheatreByIndex(index: Int): TheatreModel {
        return theatreRepository.getData()[index]
    }

    fun getTheatreById(id: Int): TheatreModel? {
        return theatreRepository.getData().find { it.id == id }
    }

    fun getUser(): List<UserModel> = userRepository.getData()

    fun getUserByName(name: String): UserModel? {
        return userRepository.getData().find { it.name == name }
    }
}