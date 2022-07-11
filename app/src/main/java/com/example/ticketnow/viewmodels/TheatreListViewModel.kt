package com.example.ticketnow.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.repository.TheatreRepository
import kotlinx.coroutines.launch

class TheatreListViewModel : ViewModel() {
    private lateinit var repository: TheatreRepository

    private var theatres = MutableLiveData<List<TheatreModel>>()

    private fun loadData() {
        viewModelScope.launch {
            theatres.postValue(repository.getData())
        }
    }

    fun initializeRepo(context: Context) {
        repository = TheatreRepository(context)
        loadData()
    }

    fun insert(name: String, location: String, totalSeats: Int, availableSeats: Int) {
        viewModelScope.launch {
            repository.insert(name, location, totalSeats, availableSeats)
        }
    }

    fun getData(): LiveData<List<TheatreModel>> = theatres

    fun updateStar(theatreId: Int, value: Int) {
        viewModelScope.launch {
            repository.updateStar(theatreId, value)
            loadData()
        }
    }

    fun getStar(theatreId: Int): Int {
        var star = -1
        viewModelScope.launch {
            star = repository.getStar(theatreId)
        }
        return star
    }
}