package com.example.ticketnow.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.ticketnow.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, MovieListFragment()).commit()

    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        when (fragment) {
            is MovieListFragment -> {
                if (doubleBackToExitPressedOnce) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity()
                    } else {
                        finish()
                    }
                }
                this.doubleBackToExitPressedOnce = true
                setSnackBar("Please click BACK again to exit")
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            }
            is BookConfirmFragment -> supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, MovieListFragment())
                .addToBackStack("tag").commit()
            else -> super.onBackPressed()
        }

    }

    private fun setSnackBar(message: String) {
        val snackBar = Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }

}