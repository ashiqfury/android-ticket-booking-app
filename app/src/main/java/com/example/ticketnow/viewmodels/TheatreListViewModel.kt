package com.example.ticketnow.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.repository.TheatreRepository

class TheatreListViewModel : ViewModel() {
    private lateinit var repository: TheatreRepository

    fun initializeRepo(context: Context) {
        repository = TheatreRepository(context)
    }

    fun insert(name: String, location: String, totalSeats: Int, availableSeats: Int) {
        repository.insert(name, location, totalSeats, availableSeats)
    }

    fun getData(): List<TheatreModel> = repository.getData()
}