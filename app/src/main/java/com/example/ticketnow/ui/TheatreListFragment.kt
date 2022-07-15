package com.example.ticketnow.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketnow.R
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.utils.StarBtnClickListener
import com.example.ticketnow.utils.TheatreRecyclerViewAdapter
import com.example.ticketnow.viewmodels.TheatreListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_theatre_view_pager.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class TheatreListFragment : Fragment() {

    enum class mSpinner { ALL, STARED }

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TheatreRecyclerViewAdapter.ViewHolder>? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var searchView: SearchView

    private lateinit var viewModel: TheatreListViewModel
    private var searchedTheatres: ArrayList<TheatreModel> = arrayListOf()
    private var theatres: ArrayList<TheatreModel> = arrayListOf()
    private var spinnerSelection = mSpinner.ALL.ordinal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TheatreListViewModel::class.java]
        viewModel.initializeRepo(requireContext())
        setHasOptionsMenu(true)
    }

    // diff util
    // shared element transaction

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_theatre_list, container, false).also { view ->
            setupBottomNavigation()
            setupAppbar()
            setupSpinner(view)
            setupRecyclerView(view)
        }
    }

    /** implementation of search in appbar */
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        val menuItem = menu.findItem(R.id.menu_search)
        searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempTheatres = arrayListOf<TheatreModel>()
                tempTheatres.addAll(searchedTheatres)
                searchedTheatres.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                /** if the search bar is not empty, something is typed so search for typed pattern in the theatres name */
                if (searchText.isNotEmpty()) {
                    tempTheatres.forEach { theatre ->
                        if (theatre.name.lowercase(Locale.getDefault()).contains(searchText)) {
                            searchedTheatres.add(theatre)
                        }
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                } else {
                    /** otherwise, if the search view is empty reset everything */
                    searchedTheatres.clear()
                    searchedTheatres.addAll(theatres)
                    recyclerView.adapter?.notifyDataSetChanged()
                    spinner.setSelection(mSpinner.ALL.ordinal) // 0 is the position for 'all' theatres
                }
                return false
            }
        })
    }

    /** return the stared value in the given theatre */
    private fun findStar(theatreId: Int): Int {
        searchedTheatres.forEach { theatre ->
            if (theatre.id == theatreId) return theatre.stared
        }
        return -1
    }

    private fun setupBottomNavigation() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
    }

    private fun setupSpinner(view: View) {
        /** implementation of spinner */
        val filter = resources.getStringArray(R.array.filter)
        spinner = view.findViewById(R.id.spinner_filter)
        spinner.let {
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, filter)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
//            it.setSelection(spinnerSelection)
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    handleChangesInSpinner(filter, position)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // perform some action
                }
            }
        }
    }

    private fun handleChangesInSpinner(filter: Array<String>, position: Int) {
        /** when the spinner has value of 'all' reset everything */
        if (position == mSpinner.ALL.ordinal) { // filter[position] == "All"
            spinnerSelection = mSpinner.ALL.ordinal
            searchedTheatres.clear()
            searchedTheatres.addAll(theatres)
            searchView.setQuery("", false)
            searchView.clearFocus()
            recyclerView.adapter?.notifyDataSetChanged()
        } else {
            /** when the spinner has value of 'stared' search for stared theatres and put that in searchTheatres array */
            spinnerSelection = mSpinner.STARED.ordinal
            val filteredTheatres = arrayListOf<TheatreModel>()
            searchedTheatres.forEach { theatre ->
                if (theatre.stared == 1) filteredTheatres.add(theatre)
            }
            searchedTheatres.clear()
            searchedTheatres.addAll(filteredTheatres)
            filteredTheatres.clear()
            searchView.setQuery("", false)
            searchView.clearFocus()
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun setupAppbar() {
        /** for appbar navigation and title */
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

    private fun setupRecyclerView(view: View) {
        /** implementation of card layout using recycler view */
        recyclerView = view.findViewById(R.id.rv_theatre)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        viewModel.theatres.observe(viewLifecycleOwner) { theatres ->
            this.theatres.clear()
            this.theatres.addAll(theatres)
            searchedTheatres.clear()
            searchedTheatres.addAll(theatres)
            recyclerView.adapter?.notifyDataSetChanged()
        }
        adapter = TheatreRecyclerViewAdapter(searchedTheatres, object : StarBtnClickListener {
            override fun clickListener(theatreId: Int, position: Int, isStar: Boolean) {
                /** if the user clicked the star, change the star and update it to the database */
                if (isStar) {
                    if (findStar(theatreId) == 1) viewModel.updateStar(theatreId, 0)
                    else viewModel.updateStar(theatreId, 1)
                    setupSpinner(view)
                    recyclerView.adapter?.notifyDataSetChanged()
                } else {
                    val fragment = TheatreViewPagerFragment()
                    val bundle = Bundle()
                    bundle.putInt("theatreId", theatreId)
                    bundle.putInt("position", position)
                    fragment.arguments = bundle

                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_layout, fragment)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        })
        recyclerView.adapter = adapter
    }
}