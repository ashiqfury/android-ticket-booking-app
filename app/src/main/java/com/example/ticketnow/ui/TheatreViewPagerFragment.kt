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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ticketnow.R
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.TheatreViewPagerAdapter
import com.example.ticketnow.viewmodels.TheatreListViewModel
import kotlinx.android.synthetic.main.fragment_movie_view_pager.*
import kotlinx.android.synthetic.main.fragment_theatre_view_pager.*


internal class TheatreViewPagerFragment : Fragment() {

    private lateinit var viewModel: TheatreListViewModel
    private var movieId: Int? = null // from movieViewpagerFragment
    private var theatreId: Int? = null // from theatreListFragment
    private var position: Int? = null // from theatreListFragment

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_theatre_view_pager, container, false)

        setupTheatreViewPager()

        return view
    }



    private fun setupTheatreViewPager() {
        viewModel.theatres.observe(viewLifecycleOwner) { theatres ->
            val adapter = TheatreViewPagerAdapter(theatres, object : BtnClickListener {
                override fun clickListener(position: Int) {
                    if (movieId != -1) {
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
                    }
                }
            })
            theatre_view_pager.adapter = adapter
            Handler(Looper.getMainLooper()).postDelayed( {
                position?.let {
                    theatre_view_pager.currentItem = it
                }
            }, 100)
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

}