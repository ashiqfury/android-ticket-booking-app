package com.example.ticketnow.viewmodels

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.data.repository.MovieListRepository
import com.example.ticketnow.data.repository.MovieRepository
import kotlinx.coroutines.*

class MovieListViewModel : ViewModel() {
    private lateinit var repository: MovieListRepository
    private var movies = MutableLiveData<List<MovieModel>>()

    fun initializeRepo(context: Context) {
        repository = MovieListRepository(context)
        loadData()
    }

    val getMoviesData: LiveData<List<MovieModel>> = movies

    private fun loadData() {
        viewModelScope.launch {
            movies.postValue(repository.getInitialData())
        }
    }
    fun fetchMoreMovies(offset: Int): LiveData<List<MovieModel>> = liveData {
        val movies = repository.fetchMoreData(offset)
        emit(movies)
    }

    fun insert(name: String, genre: String, language: String, showTime: String, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(name, genre, language, showTime, price)
        }
    }

    fun getUpdatedData(offset: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movies.postValue(repository.getUpdatedMovies(offset))
        }
    }
}