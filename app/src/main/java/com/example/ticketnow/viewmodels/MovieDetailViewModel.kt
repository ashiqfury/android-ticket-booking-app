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

class MovieDetailViewModel : ViewModel() {
    private lateinit var movieRepository: MovieRepository
    private lateinit var theatreRepository: TheatreRepository

    private var movies = MutableLiveData<List<MovieModel>>()
    private var theatres = MutableLiveData<List<TheatreModel>>()


    fun initializeRepo(context: Context) {
        movieRepository = MovieRepository(context)
        loadMovies()

        theatreRepository = TheatreRepository(context)
        loadTheatres()
    }

    private fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movies.postValue(movieRepository.getAllData())
        }
    }

    private fun loadTheatres() {
        viewModelScope.launch(Dispatchers.IO) {
            theatres.postValue(theatreRepository.getData())
        }
    }

    fun getMovies(): LiveData<List<MovieModel>> = movies

    fun getTheatres(): LiveData<List<TheatreModel>> = theatres

}