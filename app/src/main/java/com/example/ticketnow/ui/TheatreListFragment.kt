package com.example.ticketnow.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.MovieRecyclerViewAdapter
import com.example.ticketnow.utils.TheatreRecyclerViewAdapter
import com.example.ticketnow.viewmodels.MovieListViewModel
import com.example.ticketnow.viewmodels.TheatreListViewModel

class TheatreListFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TheatreRecyclerViewAdapter.ViewHolder>? = null

    private lateinit var viewModel: TheatreListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[TheatreListViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_theatre_list, container, false)

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


        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_theatre)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        viewModel.getData().observe(viewLifecycleOwner) { theatres ->
            adapter = TheatreRecyclerViewAdapter(theatres, object : BtnClickListener {
                override fun clickListener(position: Int) {

                    Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
//                    val fragment = MovieDetailFragment()
//                    val bundle = Bundle()
//                    bundle.putInt("position", position)
//                    fragment.arguments = bundle
//
//                    parentFragmentManager.beginTransaction().apply {
//                        replace(R.id.frame_layout, fragment)
//                        addToBackStack(null)
//                        commit()
//                    }
                }
            })
            recyclerView.adapter = adapter
        }
        return view
    }
}