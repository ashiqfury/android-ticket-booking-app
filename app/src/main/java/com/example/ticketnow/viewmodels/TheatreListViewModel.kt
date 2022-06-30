package com.example.ticketnow.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.repository.TheatreRepository

class TheatreListViewModel : ViewModel() {
    private lateinit var repository: TheatreRepository

    private var theatres = MutableLiveData<List<TheatreModel>>()

    private fun loadData() {
        theatres.value = repository.getData()
    }

    fun initializeRepo(context: Context) {
        repository = TheatreRepository(context)
        loadData()
    }

    fun insert(name: String, location: String, totalSeats: Int, availableSeats: Int) {
        repository.insert(name, location, totalSeats, availableSeats)
    }

    fun getData(): LiveData<List<TheatreModel>> = theatres

}