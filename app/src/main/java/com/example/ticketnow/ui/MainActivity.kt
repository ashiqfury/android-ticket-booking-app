package com.example.ticketnow.ui

import android.content.ContentValues.TAG
import android.database.DatabaseUtils
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ticketnow.R
import com.example.ticketnow.data.repository.MovieRepository
import com.example.ticketnow.data.repository.TheatreRepository
import com.example.ticketnow.data.repository.TicketBookingRepository
import com.example.ticketnow.data.repository.remote.FakeTheatreRemoteDB
import com.example.ticketnow.utils.DatabaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation()



//        runBlocking {
//            val theatres = FakeTheatreRemoteDB.getAllTheatres(this@MainActivity)
//            theatres.forEach {
//                Log.d("TICKET_NOW", "onCreate: $it")
//            }
//        }

        val helper = DatabaseHelper(this)
//        runBlocking {
//            helper.deleteAllTheatres()
//        }

        runBlocking {
//            FakeTheatreRemoteDB.getAllTheatres(this@MainActivity)
//            helper.deleteAllMovies()
//            helper.deleteAllTheatres()
//            helper.insertTheatre("PVR Cinemas", "Madurai", 20, 20, 0)
            val theatreRepository = TheatreRepository(this@MainActivity)
            theatreRepository.getTheatresFromDB()
//            theatreRepository.getData().forEach {
//                Log.d("TICKET_NOW", "onCreate: $it ")
//            }
//            val cursor = helper.getAllTheatres()

//            Log.d("TICKET_NOW",  "onCreate: ${DatabaseUtils.dumpCursorToString(cursor)}")
        }


//        helper.updateStar("1", 1)
//        val movieRepo = MovieRepository(this)
//        val movies = movieRepo.getMoviesFromNetwork()

//        val fileInString: String = applicationContext.assets.open("moviesdata.json").bufferedReader().use { it.readText() }

//        movies.forEach {
//            Log.d("TICKETNOW_FURY", "onCreate: $it")
//        }

//        movieRepo.getMoviesFromDB().forEach {
//            helper.deleteMovie(it.id.toString())
//        }

//        Log.d(TAG, "Star in theatre 1 = $star")
//        val data = theatreRepository.getData()

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, MovieListFragment()).commit()
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.movies_tab -> replaceCurrentFragment(MovieListFragment())
                R.id.theatres_tab -> replaceCurrentFragment(TheatreListFragment())
            }
            true
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        when (fragment) {
            is MovieListFragment, is TheatreListFragment -> {
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

    private fun replaceCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

}