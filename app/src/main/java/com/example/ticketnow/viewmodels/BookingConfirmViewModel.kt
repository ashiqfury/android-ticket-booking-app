package com.example.ticketnow.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class BookingConfirmViewModel : ViewModel() {

    private lateinit var bookingRepository: TicketBookingRepository
    private lateinit var userRepository: UserRepository
    private lateinit var movieRepository: MovieRepository
    private lateinit var theatreRepository: TheatreRepository
    private lateinit var users: MutableLiveData<List<UserModel>>
    private lateinit var movies: MutableLiveData<List<MovieModel>>
    private lateinit var theatres: MutableLiveData<List<TheatreModel>>
    private lateinit var bookings: MutableLiveData<List<BookTicketModel>>

    fun initializeRepo(context: Context) {
        bookingRepository = TicketBookingRepository(context)
        userRepository = UserRepository(context)
        movieRepository = MovieRepository(context)
        theatreRepository = TheatreRepository(context)
        loadBookings()
        loadUsers()
        loadMovies()
        loadTheatres()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            bookings.postValue(bookingRepository.getData())
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            users.postValue(userRepository.getData())
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            movies.postValue(movieRepository.getData())
        }
    }

    private fun loadTheatres() {
        viewModelScope.launch {
            theatres.postValue(theatreRepository.getData())
        }
    }

    fun getBookings(): LiveData<List<BookTicketModel>> = bookings

    fun getUsers(): LiveData<List<UserModel>> = users

    fun getMovies(): LiveData<List<MovieModel>> = movies

    fun getTheatres(): LiveData<List<TheatreModel>> = theatres

}