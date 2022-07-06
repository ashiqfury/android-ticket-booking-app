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

    fun insert(name: String, number: Long): Long {
        return try {
            helper.insertUser(name, number)
        } catch (e: Exception){
            e.printStackTrace()
            -1
        }

    }

    fun getData(): List<UserModel> {

        val list = mutableListOf<UserModel>()
        val cursor = helper.getAllUsers

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id: String = cursor.getString(cursor.getColumnIndex("id"))
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val number: String = cursor.getString(cursor.getColumnIndex("phoneNumber"))
                val user = UserModel(id.toInt(), name, number.toLong())
                list.add(user)
                cursor.moveToNext()
            }
        }
        return list
    }

    fun getUser(userId: Int): UserModel {
        var user: UserModel? = null
        val cursor = helper.getUser(userId)
        cursor.moveToFirst()
        if (cursor.moveToFirst()) {
            val id: String = cursor.getString(cursor.getColumnIndex("id"))
            val name: String = cursor.getString(cursor.getColumnIndex("name"))
            val number: String = cursor.getString(cursor.getColumnIndex("phoneNumber"))
            user = UserModel(id.toInt(), name, number.toLong())
            cursor.moveToNext()
        }
        return user!!
    }
}