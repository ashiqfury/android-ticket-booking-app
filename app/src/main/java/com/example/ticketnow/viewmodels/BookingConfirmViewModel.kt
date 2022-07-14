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

    private val _bookings = MutableLiveData<List<BookTicketModel>>()
    private val _theatres = MutableLiveData<List<TheatreModel>>()
    private val _movies = MutableLiveData<List<MovieModel>>()
    private val _users = MutableLiveData<List<UserModel>>()

    val bookings: LiveData<List<BookTicketModel>> = _bookings
    val theatres: LiveData<List<TheatreModel>> = _theatres
    val movies: LiveData<List<MovieModel>> = _movies
    val users: LiveData<List<UserModel>> = _users

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
            bookingRepository.getAllBookings {
                _bookings.postValue(it)
            }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _users.postValue(userRepository.getData())
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _movies.postValue(movieRepository.getAllData())
        }
    }

    private fun loadTheatres() {
        viewModelScope.launch {
            _theatres.postValue(theatreRepository.getData())
        }
    }
}