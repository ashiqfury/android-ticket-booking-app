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

    private var _theatres = MutableLiveData<List<TheatreModel>>()
    val theatres: LiveData<List<TheatreModel>> = _theatres

    private fun loadData() {
        viewModelScope.launch {
            _theatres.postValue(repository.getData())
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


    fun updateStar(theatreId: Int, value: Int) {
        viewModelScope.launch {
            repository.updateStar(theatreId, value)
            loadData()
        }
    }
}