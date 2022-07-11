package com.example.ticketnow.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {
    private lateinit var repository: MovieRepository
    private var movies = MutableLiveData<List<MovieModel>>()

    fun initializeRepo(context: Context) {
        repository = MovieRepository(context)
        loadData()
    }

    val getMoviesData: LiveData<List<MovieModel>> = movies


    private fun loadData() {
        viewModelScope.launch {
            movies.postValue(repository.getAllData())
        }
    }

    /*fun getData(page: Int, limit: Int): List<MovieModel> {
        lateinit var movies: List<MovieModel>
        viewModelScope.launch {
            movies = repository.getData(page, limit)
        }
        return movies
    }*/

    suspend fun insert(name: String, genre: String, language: String, showTime: String, price: Int) {
        viewModelScope.launch {
            repository.insert(name, genre, language, showTime, price)
        }
    }

    fun getUpdatedData() {
        viewModelScope.launch(Dispatchers.IO) {
            movies.postValue(repository.getUpdatedMovies())
        }
    }
}