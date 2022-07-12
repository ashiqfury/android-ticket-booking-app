package com.example.ticketnow.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ticketnow.R
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.repository.MovieListRepository
import com.example.ticketnow.utils.MovieRecyclerViewAdapter
import com.example.ticketnow.utils.RecyclerViewClickListener
import com.example.ticketnow.viewmodels.MovieListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.collections.ArrayList

internal class MovieListFragment : Fragment() {

    private lateinit var adapter: RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: MovieListViewModel

    private var allMovies: ArrayList<MovieModel> = arrayListOf()
    private var searchedMovies: ArrayList<MovieModel> = arrayListOf()
    private var movies: ArrayList<MovieModel> = arrayListOf()
    private var offset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
        viewModel.initializeRepo(requireContext())
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_movie_list, container, false)
        setupAddBar()
        setupBottomNavigation(view)
        initializeValues(view)
        observeLiveData()
        setupNestedScrollView()
        setupRecyclerView(searchedMovies)
        return view
    }

    private fun setupRecyclerView(movies: List<MovieModel>) {
        adapter = MovieRecyclerViewAdapter(movies, object : RecyclerViewClickListener {
            override fun clickListener(position: Int, isButton: Boolean) {
                if (isButton) {
                    val bundle = Bundle()
                    bundle.putInt("movieId", position)
                    TheatreViewPagerFragment().apply {
                        arguments = bundle
                        navigateFragment(this, true)
                    }
                }
                else {
                    val bundle = Bundle()
                    bundle.putInt("position", position)
                    MovieDetailFragment().apply {
                        arguments = bundle
                        navigateFragment(this, true)
                    }
                }
            }
        })
        recyclerView.adapter = adapter
    }

    private fun setupNestedScrollView() {
        nestedScrollView.viewTreeObserver.addOnScrollChangedListener(object: ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                val scrollView = nestedScrollView.getChildAt(nestedScrollView.childCount - 1)
                val diff = scrollView.bottom - (nestedScrollView.height + nestedScrollView.scrollY)
                Log.d("TICKET_NOW", "onScrollChanged: DIFFERENCE $diff ")
                if (diff == 0) {
                    Log.d("TICKET_NOW", "onScrollChanged: DIFFERENCE $diff ")
                    Log.d("TICKET_NOW", "onScrollChanged: MOVIES ${movies.size} ")
                    progressBar.visible()
                    loadMoreData()
                }
            }
        })
    }

    private fun loadMoreData() {
        offset += 10
        viewModel.fetchMoreMovies(offset).observe(viewLifecycleOwner) {
            searchedMovies.addAll(it)
        }
        progressBar.hide()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun initializeValues(view: View) {
        nestedScrollView = view.findViewById(R.id.scroll_view)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeLiveData() {
        viewModel.getMoviesData.observe(viewLifecycleOwner) { movies ->
            this.movies.clear()
            this.movies.addAll(movies)
            searchedMovies.clear()
            searchedMovies.addAll(movies)
            allMovies.addAll(movies)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipe)
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed( {
                viewModel.getUpdatedData(offset = 0)
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }
    }

    @Deprecated("Deprecated in Java")
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

    private fun setupBottomNavigation(view: View) {
        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.movie_bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.theatres_tab -> navigateFragment(TheatreListFragment())
                R.id.movies_tab -> navigateFragment(MovieListFragment())
            }
            true
        }
    }

    private fun navigateFragment(fragment: Fragment, backStack: Boolean = false) {
        if (backStack) {
            parentFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(null).commit()
        } else {
            parentFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
        }
    }

    private fun setupAddBar() {
        if (activity != null) {
            (activity as MainActivity).title = "Popcorn Cinemas"
        }
        if ((activity as MainActivity).supportActionBar != null) {
            val actionBar = (activity as MainActivity).supportActionBar
            actionBar?.let {
                it.setDisplayHomeAsUpEnabled(false)
                it.setHomeButtonEnabled(false)
            }
        }
    }

    private fun ProgressBar.visible() {
        this.visibility = View.VISIBLE
    }
    private fun ProgressBar.hide() {
        this.visibility = View.GONE
    }
}