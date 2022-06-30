package com.example.ticketnow.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.ticketnow.R
import org.w3c.dom.Text

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val titleFirst: TextView = findViewById(R.id.splash_title_1)
        val animFirstTitle = AnimationUtils.loadAnimation(this, R.anim.title_first_animation)
        titleFirst.startAnimation(animFirstTitle)

        val titleSecond: TextView = findViewById(R.id.splash_title_2)
        val animSecondTitle = AnimationUtils.loadAnimation(this, R.anim.title_second_animation)
        titleSecond.startAnimation(animSecondTitle)

        val slogan: TextView = findViewById(R.id.splash_slogan)
        val animSlogan = AnimationUtils.loadAnimation(this, R.anim.slogan_animation)
        slogan.startAnimation(animSlogan)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)

/*        val helper = DatabaseHelper(this)

        helper.insertMovie("Titanic", "Romance", "Tamil", "Night", 120)
        helper.insertTheatre("PVC Cinemas", "Tirunelveli", 20, 20)

        val theatres = helper.getAllTheatres
        theatres.moveToFirst()

        val movies = helper.getAllMovies
        movies.moveToFirst()

        Log.d("TAG_FURY_THEATRES", DatabaseUtils.dumpCursorToString(theatres))
        Log.d("TAG_FURY_MOVIES", DatabaseUtils.dumpCursorToString(movies))*/
    }
}