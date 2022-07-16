package com.example.ticketnow.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.ticketnow.R
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.MovieMiniRecyclerViewAdapter
import com.example.ticketnow.utils.TheatreMiniRecyclerViewAdapter
import com.example.ticketnow.utils.TheatreViewPagerAdapter
import com.example.ticketnow.viewmodels.TheatreListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_movie_view_pager.*
import kotlinx.android.synthetic.main.fragment_theatre_view_pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


internal class TheatreViewPagerFragment : Fragment() {

    private var adapter: RecyclerView.Adapter<MovieMiniRecyclerViewAdapter.ViewHolder>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var viewModel: TheatreListViewModel
    private var movieId: Int? = null // from movieViewpagerFragment
    private var theatreId: Int? = null // from theatreListFragment
    private var position: Int? = null // from theatreListFragment
    private var currentViewPagerPosition = -1
    private lateinit var currentTheatre: TheatreModel
    private var recyclerViewMovies = arrayListOf<MovieModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TheatreListViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        val bundle = this.arguments
        if (bundle != null) {
            this.movieId = bundle.getInt("movieId", -1)
            this.theatreId = bundle.getInt("theatreId", -1)
            this.position = bundle.getInt("position", -1)
        }

        if (activity != null) {
            (activity as MainActivity).title = "Theatres List"
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_theatre_view_pager, container, false)
        viewPager2 = view.findViewById(R.id.theatre_view_pager)
        handleBottomNavigation()
        setupTheatreViewPager(view)
        setupMiniMoviesList(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (movieId == -1) { // starts from theatres list
            view.findViewById<Button>(R.id.theatre_btn_book_ticket).visibility = View.GONE
        } else {
            view.findViewById<LinearLayoutCompat>(R.id.theatre_extras_layout).visibility = View.GONE
        }
    }

    private fun setupTheatreViewPager(view: View) {
        viewModel.theatres.observe(viewLifecycleOwner) { theatres ->
            val adapter = TheatreViewPagerAdapter(theatres, object : BtnClickListener {
                override fun clickListener(position: Int) {
                    /*if (movieId != -1) {
                        val fragment = BookingDetailFragment()
                        val bundle = Bundle()
                        bundle.putInt("movieId", movieId ?: 0)
                        bundle.putInt("theatreId", theatres[position].id)
                        fragment.arguments = bundle

                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.frame_layout, fragment)
                            addToBackStack(null)
                            commit()
                        }
                    }
                    else {
                        Toast.makeText(requireContext(), "New functionality", Toast.LENGTH_SHORT).show()
                    }*/
                }
            })
            theatre_view_pager.adapter = adapter
            Handler(Looper.getMainLooper()).postDelayed( {
                position?.let {
                    theatre_view_pager.currentItem = it
                }
            }, 100)
            view.findViewById<Button>(R.id.theatre_btn_book_ticket).setOnClickListener {
                val fragment = BookingDetailFragment()
                val bundle = Bundle()
                val position = theatre_view_pager.currentItem
                bundle.putInt("movieId", movieId ?: 0)
                bundle.putInt("theatreId", theatres[position].id)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_layout, fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentViewPagerPosition = position
                viewModel.theatres.observe(viewLifecycleOwner) {
                    theatreId = it[currentViewPagerPosition].id
                    currentTheatre = it[currentViewPagerPosition]
                    getMoviesListFromTheatre()
                    Log.d("TICKET_NOW", "onPageSelected: $recyclerViewMovies")
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setupMiniMoviesList(view: View) {

        recyclerView = view.findViewById(R.id.rv_show_more_movie)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val showMore = view.findViewById<TextView>(R.id.theatre_show_more)
        showMore.setOnClickListener {
//            setTheatreIdFromViewPagerPosition()
            theatreId?.let { theatreId -> navigateToMovieListFragment(theatreId) }
        }

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isEmpty()) showMore.text = ""

            adapter = MovieMiniRecyclerViewAdapter(recyclerViewMovies, object : BtnClickListener {
                override fun clickListener(position: Int) {
//                    setTheatreIdFromViewPagerPosition()
                    theatreId?.let { theatreId -> navigateToBookingDetailFragment(theatreId, movies[position].id) }
                }
            })

            recyclerView.adapter = adapter
        }
    }

    private fun navigateToMovieListFragment(theatreId: Int) {
        val fragment = MovieViewPagerFragment()
        val bundle = Bundle()
        bundle.putInt("theatreId", theatreId)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == android.R.id.home) {
            parentFragmentManager.popBackStack()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun handleBottomNavigation() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE
    }

    private fun setTheatreIdFromViewPagerPosition() {
        viewModel.theatres.observe(viewLifecycleOwner) {
            theatreId = it[theatre_view_pager.currentItem].id
        }
    }

    private fun getMoviesListFromTheatre() {
        recyclerViewMovies.clear()

        val listOfMovieId = currentTheatre.moviesList
        Log.d("TICKET_NOW", "currentTheatre $currentTheatre")
//        val movies = arrayListOf<MovieModel>()
        viewModel.movies.observe(viewLifecycleOwner) { _movies ->
            _movies.forEach { movie ->
//                if (listOfMovieId.contains(movie.id)) {
//                    Log.d("TICKET_NOW", "ids: $listOfMovieId id ${movie.id}")
//                    recyclerViewMovies.add(movie)
//                }
            }
        }
    }
}