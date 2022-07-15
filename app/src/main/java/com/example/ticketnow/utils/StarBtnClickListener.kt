package com.example.ticketnow.utils

interface StarBtnClickListener {
    fun clickListener(theatreId: Int, position: Int, isStar: Boolean)
}