package com.example.ticketnow.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.MovieAdapter
import com.example.ticketnow.viewmodels.MovieListViewModel

internal class MovieListFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MovieAdapter.ViewHolder>? = null

    private lateinit var viewModel: MovieListViewModel

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
            actionBar!!.setDisplayHomeAsUpEnabled(false)
            actionBar.setHomeButtonEnabled(false)
        }


        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        viewModel.getData().observe(viewLifecycleOwner) { movies ->
            adapter = MovieAdapter(movies, object : BtnClickListener {
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
        }
        return view
    }

}