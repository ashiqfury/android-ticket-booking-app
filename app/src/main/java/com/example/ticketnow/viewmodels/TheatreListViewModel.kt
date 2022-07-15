package com.example.ticketnow.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.repository.MovieRepository
import com.example.ticketnow.data.repository.TheatreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TheatreListViewModel : ViewModel() {
    private lateinit var theatreRepository: TheatreRepository
    private lateinit var movieRepository: MovieRepository

    private var _theatres = MutableLiveData<List<TheatreModel>>()
    private var _movies = MutableLiveData<List<MovieModel>>()
    val theatres: LiveData<List<TheatreModel>> = _theatres
    val movies: LiveData<List<MovieModel>> = _movies


    private fun loadTheatres() {
        viewModelScope.launch {
            _theatres.postValue(theatreRepository.getData())
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _movies.postValue(movieRepository.getAllData())
        }
    }

    fun initializeRepo(context: Context) {
        theatreRepository = TheatreRepository(context)
        movieRepository = MovieRepository(context)
        loadTheatres()
        loadMovies()
    }

    fun insert(name: String, location: String, totalSeats: Int, availableSeats: Int) {
        viewModelScope.launch {
            theatreRepository.insert(name, location, totalSeats, availableSeats)
        }
    }


    fun updateStar(theatreId: Int, value: Int) {
        viewModelScope.launch {
            theatreRepository.updateStar(theatreId, value)
            loadTheatres()
        }
    }
}

// replica the viewModel and build the app to see the theatres viewpager layout.