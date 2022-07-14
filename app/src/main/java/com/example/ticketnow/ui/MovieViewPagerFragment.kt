package com.example.ticketnow.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.MovieViewPagerAdapter
import com.example.ticketnow.utils.TheatreMiniRecyclerViewAdapter
import com.example.ticketnow.viewmodels.MovieDetailViewModel
import kotlinx.android.synthetic.main.fragment_movie_view_pager.*

class MovieViewPagerFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel
    private var movieId: Int? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TheatreMiniRecyclerViewAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        val bundle = this.arguments
        if (bundle != null) {
            this.movieId = bundle.getInt("position", 0)
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
        return inflater.inflate(R.layout.fragment_movie_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            val adapter = MovieViewPagerAdapter(movies, object: BtnClickListener {
                override fun clickListener(position: Int) {
                    Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
                }
            })
            movie_view_pager.adapter = adapter
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_show_more_theatre)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val showMore = view.findViewById<TextView>(R.id.movie_show_more)
        showMore.setOnClickListener {
//            navigateToTheatreListFragment(movie.id)
        }

        viewModel.theatres.observe(viewLifecycleOwner) { theatres ->
            if (theatres.isEmpty()) showMore.text = ""

            adapter = TheatreMiniRecyclerViewAdapter(theatres, object : BtnClickListener {
                override fun clickListener(position: Int) {
//                    navigateToBookingDetailFragment(movie.id, theatres[position].id)
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
}