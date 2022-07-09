package com.example.ticketnow.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    private lateinit var repository: MovieRepository

    private var movies = MutableLiveData<List<MovieModel>>()

    private fun loadData() {
        viewModelScope.launch {
            movies.postValue(repository.getData())
        }
    }

    fun initializeRepo(context: Context) {
        repository = MovieRepository(context)
        loadData()
    }

    fun insert(name: String, genre: String, language: String, showTime: String, price: Int) {
        repository.insert(name, genre, language, showTime, price)
    }

    fun getData(): LiveData<List<MovieModel>> = movies

    fun getUpdatedData() {
        viewModelScope.launch {
            movies.postValue(repository.getMoviesFromNetwork())
        }
    }
}