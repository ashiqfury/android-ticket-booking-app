package com.example.ticketnow.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.repository.MovieListRepository
import kotlinx.coroutines.*

class MovieListViewModel : ViewModel() {
    private lateinit var repository: MovieListRepository
    private val _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>> = _movies
    private var getUpdatedMoviesJob: Job? = null
    private var fetchMoreMoviesJob: Job? = null

    fun initializeRepo(context: Context) {
        repository = MovieListRepository(context)
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getInitialData { movies ->
                _movies.postValue(movies)
            }
        }
    }

    /**
     * Swipe to Refresh
     */
    fun getUpdatedMovies(offset: Int) {
        if (getUpdatedMoviesJob?.isActive == true) getUpdatedMoviesJob?.cancel()

        getUpdatedMoviesJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getUpdatedMovies(offset) { movies ->
                _movies.postValue(movies)
            }
        }
    }

    /**
     * Load more while scroll down list
     */
    fun fetchMoreMovies(offset: Int) = liveData {
//        if (fetchMoreMoviesJob?.isActive == true) fetchMoreMoviesJob?.cancel()
//        getUpdatedMoviesJob = viewModelScope.launch(Dispatchers.Main) {
            val movies = repository.fetchMoreData(offset)
            emit(movies)
//        }
    }

    fun insert(name: String, genre: String, language: String, showTime: String, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(name, genre, language, showTime, price)
        }
    }
}