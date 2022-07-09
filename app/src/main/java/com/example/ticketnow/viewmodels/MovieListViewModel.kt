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
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {
    private lateinit var repository: MovieRepository
    private var movies = MutableLiveData<List<MovieModel>>()

    fun initializeRepo(context: Context) {
        repository = MovieRepository(context)
        loadData()
    }

    fun getMoviesData(): LiveData<List<MovieModel>> = movies

    private fun loadData() {
        viewModelScope.launch(Dispatchers.Default) {
            movies.postValue(repository.getData())
        }
    }

    suspend fun insert(name: String, genre: String, language: String, showTime: String, price: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insert(name, genre, language, showTime, price)
        }
    }

    fun getUpdatedData() {
        viewModelScope.launch(Dispatchers.Default) {
            movies.postValue(repository.getUpdatedMovies())
        }
    }
}