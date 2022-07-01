package com.example.ticketnow.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.MovieRecyclerViewAdapter
import com.example.ticketnow.viewmodels.MovieListViewModel
import kotlinx.android.synthetic.main.fragment_movie_list.*
import java.util.*
import kotlin.collections.ArrayList

internal class MovieListFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>? = null
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: MovieListViewModel
    private var searchedMovies: ArrayList<MovieModel> = arrayListOf()
    private var movies: ArrayList<MovieModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_movie_list, container, false)


        if (activity != null) {
            (activity as MainActivity).title = "Popular Cinemas"
        }
        if ((activity as MainActivity).supportActionBar != null) {
            val actionBar = (activity as MainActivity).supportActionBar
            actionBar?.let {
                it.setDisplayHomeAsUpEnabled(false)
                it.setHomeButtonEnabled(false)
            }
        }


        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        viewModel.getData().observe(viewLifecycleOwner) { movies ->
            this.movies.clear()
            searchedMovies.clear()
            searchedMovies.addAll(movies)
            this.movies.addAll(movies)
        }
        adapter = MovieRecyclerViewAdapter(searchedMovies, object : BtnClickListener {
            override fun clickListener(position: Int) {

                val fragment = MovieDetailFragment()
                val bundle = Bundle()
                bundle.putInt("position", position)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_layout, fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        })
        recyclerView.adapter = adapter

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        val menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchedMovies.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    movies.forEach { movie ->
                        if (movie.name.lowercase(Locale.getDefault()).contains(searchText)) {
                            searchedMovies.add(movie)
                            Log.d("FURY", movie.name)
                        }
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                } else {
                    searchedMovies.clear()
                    searchedMovies.addAll(movies)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                return false
            }
        })

    }

}