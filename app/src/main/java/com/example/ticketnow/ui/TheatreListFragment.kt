package com.example.ticketnow.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.MovieRecyclerViewAdapter
import com.example.ticketnow.utils.TheatreRecyclerViewAdapter
import com.example.ticketnow.viewmodels.MovieListViewModel
import com.example.ticketnow.viewmodels.TheatreListViewModel
import java.util.*
import kotlin.collections.ArrayList

class TheatreListFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TheatreRecyclerViewAdapter.ViewHolder>? = null
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: TheatreListViewModel
    private var searchedTheatres: ArrayList<TheatreModel> = arrayListOf()
    private var theatres: ArrayList<TheatreModel> = arrayListOf()

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


        recyclerView = view.findViewById(R.id.rv_theatre)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        viewModel.getData().observe(viewLifecycleOwner) { theatres ->
            this.theatres.clear()
            this.theatres.addAll(theatres)
            searchedTheatres.clear()
            searchedTheatres.addAll(theatres)
            recyclerView.adapter?.notifyDataSetChanged()
        }
        adapter = TheatreRecyclerViewAdapter(searchedTheatres, object : BtnClickListener {
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
                searchedTheatres.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    theatres.forEach { theatre ->
                        if (theatre.name.lowercase(Locale.getDefault()).contains(searchText)) {
                            searchedTheatres.add(theatre)
                            Log.d("FURY", theatre.name)
                        }
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                } else {
                    searchedTheatres.clear()
                    searchedTheatres.addAll(theatres)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                return false
            }
        })

    }
}