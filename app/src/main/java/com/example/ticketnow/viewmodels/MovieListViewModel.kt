package com.example.ticketnow.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.repository.MovieListRepository
import kotlinx.coroutines.*

class MovieListViewModel : ViewModel() {
    private lateinit var repository: MovieListRepository
    private val _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>> = _movies
//    private var

    fun initializeRepo(context: Context) {
        repository = MovieListRepository(context)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _movies.postValue(repository.getInitialData())
        }
    }

    /**
     * Load more while scroll down list
     */
    fun fetchMoreMovies(offset: Int): LiveData<List<MovieModel>> = liveData {
        val movies = repository.fetchMoreData(offset)
        emit(movies)
    }

    fun insert(name: String, genre: String, language: String, showTime: String, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(name, genre, language, showTime, price)
        }
    }

    /**
     * Swipe to Refresh
     */
    fun getUpdatedData(offset: Int) {
        val job: Job = viewModelScope.launch(Dispatchers.IO) {
            Log.d("TICKET_NOW", "getUpdatedData: ")
            _movies.postValue(repository.getUpdatedMovies(offset))
        }
        viewModelScope.launch {
            if (job.isActive) {
                job.cancelAndJoin()
            }
        }
    }
}