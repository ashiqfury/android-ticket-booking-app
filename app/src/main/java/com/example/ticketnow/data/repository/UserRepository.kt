package com.example.ticketnow.data.repository

import android.content.Context
import android.database.DatabaseUtils
import android.util.Log
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.utils.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(context: Context) {
    private val helper = DatabaseHelper(context)
    private val TAG = "BOOK_MY_MOVIE"

    fun insert(name: String, number: Long) = CoroutineScope(
        Dispatchers.Main).launch {
        try {
            helper.insertUser(name, number)
            Log.d("BOOK_MY_SHOW", "REPOSITORY CALLING $name $number")
            Log.d(TAG, "Data inserted successfully")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getDataByName(nameToSearch: String): UserModel? {
        val cursor = helper.getUserByName(nameToSearch)

        if (cursor.moveToFirst()) {
            Log.d("BOOKING_TAG", DatabaseUtils.dumpCursorToString(cursor))
            while (!cursor.isAfterLast) {
                val id: String = cursor.getString(cursor.getColumnIndex("id"))
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val number: String = cursor.getString(cursor.getColumnIndex("phoneNumber"))
                val user = UserModel(id.toInt(), name, number.toLong())
                cursor.moveToNext()
                return user
            }
        }
        return null
    }

    fun getData(): List<UserModel> {

        val list = mutableListOf<UserModel>()
        val cursor = helper.getAllUsers

        if (cursor.moveToFirst()) {
            Log.d("BOOKING_TAG", DatabaseUtils.dumpCursorToString(cursor))
            while (!cursor.isAfterLast) {
                val id: String = cursor.getString(cursor.getColumnIndex("id"))
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val number: String = cursor.getString(cursor.getColumnIndex("phoneNumber"))
                val user = UserModel(id.toInt(), name, number.toLong())
                Log.d("BOOK", user.toString())
                list.add(user)
                cursor.moveToNext()
            }
        }
        return list
    }
}