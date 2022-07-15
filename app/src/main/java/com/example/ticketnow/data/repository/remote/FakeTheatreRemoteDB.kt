package com.example.ticketnow.data.repository.remote

import android.content.Context
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FakeTheatreRemoteDB : TheatreRemoteInterface {

    override suspend fun getAllTheatres(context: Context): List<TheatreModel> {
        val gson = Gson()

        val theatreType = object : TypeToken<List<TheatreModel>>() {}.type
        val theatresJsonString: String =
            context.assets.open("theatresData.json").bufferedReader().use { it.readText() }

        return gson.fromJson(theatresJsonString, theatreType)
    }
}