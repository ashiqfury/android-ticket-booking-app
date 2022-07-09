package com.example.ticketnow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.TheatreViewPagerAdapter
import com.example.ticketnow.viewmodels.MovieDetailViewModel


class MovieDetailFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movie: MovieModel
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TheatreViewPagerAdapter.ViewHolder>? = null
    private var movieId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        val bundle = this.arguments
        if (bundle != null) {
            this.movieId = bundle.getInt("position", 0)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_movie_detail, container, false)


        if (activity != null) {
            (activity as MainActivity).title = "Movie Details"
        }
        if ((activity as MainActivity).supportActionBar != null) {
            val actionBar = (activity as MainActivity).supportActionBar
            actionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeButtonEnabled(true)
            }
        }

        viewModel.getMovies().observe(viewLifecycleOwner) { _movies ->
            _movies.find { it.id == movieId }?.let { _movie ->
                this.movie = _movie
                assignValuesToViews(view)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_show_more_theatre)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val showMore = view.findViewById<TextView>(R.id.movie_show_more)
        showMore.setOnClickListener {
            navigateToTheatreListFragment(movie.id)
        }

        viewModel.getTheatres().observe(viewLifecycleOwner) { theatres ->
            if (theatres.isEmpty()) showMore.text = ""

            adapter = TheatreViewPagerAdapter(theatres, object : BtnClickListener {
                override fun clickListener(position: Int) {
                    navigateToBookingDetailFragment(movie.id, theatres[position].id)
                }
            })

            recyclerView.adapter = adapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == android.R.id.home) {
            parentFragmentManager.popBackStack()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToTheatreListFragment(movieId: Int) {
        val fragment = TheatreViewPagerFragment()
        val bundle = Bundle()
        bundle.putInt("movieId", movieId)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun navigateToBookingDetailFragment(movieId: Int, theatreId: Int) {
        val fragment = BookingDetailFragment()
        val bundle = Bundle()
        bundle.putInt("movieId", movieId)
        bundle.putInt("theatreId", theatreId)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
            commit()
        }
    }


    private fun assignValuesToViews(view: View) {
        if (::movie.isInitialized) {
            view.findViewById<TextView>(R.id.movie_title).text = movie.name
            view.findViewById<TextView>(R.id.movie_desc).text = "This is the description of the movie"
            view.findViewById<TextView>(R.id.movie_language).text = "Language: ${movie.language}"
            view.findViewById<TextView>(R.id.movie_genre).text = "Genre: ${movie.genre}"
            view.findViewById<TextView>(R.id.movie_showtime).text = "Show Time: ${movie.time}"
            view.findViewById<TextView>(R.id.movie_price).text = "Price: ${movie.price}"
        }
    }
}
