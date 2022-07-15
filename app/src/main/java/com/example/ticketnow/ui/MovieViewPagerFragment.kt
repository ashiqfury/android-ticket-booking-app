package com.example.ticketnow.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.MovieViewPagerAdapter
import com.example.ticketnow.utils.TheatreMiniRecyclerViewAdapter
import com.example.ticketnow.viewmodels.MovieDetailViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_movie_view_pager.*

class MovieViewPagerFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel
    private var movieId: Int? = null
    private var position: Int? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TheatreMiniRecyclerViewAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        val bundle = this.arguments
        if (bundle != null) {
            this.movieId = bundle.getInt("movieId", 1)
            this.position = bundle.getInt("position", 1)
        }

        if (activity != null) {
            (activity as MainActivity).title = "Movies List"
        }
        if ((activity as MainActivity).supportActionBar != null) {
            val actionBar = (activity as MainActivity).supportActionBar
            actionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeButtonEnabled(true)
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_movie_view_pager, container, false)

        setupMoviesViewPager()
        setupMiniTheatresList(view)
        handleBottomNavigation()

        return view
    }

    private fun setupMoviesViewPager() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            val adapter = MovieViewPagerAdapter(movies, object: BtnClickListener {
                override fun clickListener(position: Int) {
                    Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
                }
            })
            movie_view_pager.adapter = adapter
            Handler(Looper.getMainLooper()).postDelayed( {
                position?.let {
                    movie_view_pager.currentItem = it
                }
            }, 100)
        }
    }

    private fun setupMiniTheatresList(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_show_more_theatre)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val showMore = view.findViewById<TextView>(R.id.movie_show_more)
        showMore.setOnClickListener {
            setMovieIdFromViewPagerPosition()
            movieId?.let { movieId -> navigateToTheatreListFragment(movieId) }
        }

        viewModel.theatres.observe(viewLifecycleOwner) { theatres ->
            if (theatres.isEmpty()) showMore.text = ""

            adapter = TheatreMiniRecyclerViewAdapter(theatres, object : BtnClickListener {
                override fun clickListener(position: Int) {
                    setMovieIdFromViewPagerPosition()
                    movieId?.let { movieId -> navigateToBookingDetailFragment(movieId, theatres[position].id) }
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

    private fun handleBottomNavigation() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE
    }

    private fun setMovieIdFromViewPagerPosition() {
        viewModel.movies.observe(viewLifecycleOwner) {
            movieId = it[movie_view_pager.currentItem].id
        }
    }
}