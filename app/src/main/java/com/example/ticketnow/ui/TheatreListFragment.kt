package com.example.ticketnow.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ticketnow.R
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.utils.BtnClickListener
import com.example.ticketnow.utils.ViewPagerAdapter
import com.example.ticketnow.viewmodels.TheatreListViewModel
import kotlinx.android.synthetic.main.fragment_theatre_list.*


internal class TheatreListFragment : Fragment() {

    private lateinit var viewModel: TheatreListViewModel
    private var movieId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TheatreListViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        val bundle = this.arguments
        if (bundle != null) {
            this.movieId = bundle.getInt("movieId", 0)
        }

        if (activity != null) {
            (activity as MainActivity).title = "Theatres List"
        }
        if ((activity as MainActivity).supportActionBar != null) {
            val actionBar = (activity as MainActivity).supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_theatre_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner) { theatres ->
            val adapter = ViewPagerAdapter(theatres, object : BtnClickListener {
                override fun clickListener(position: Int) {
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
            })
            view_pager.adapter = adapter
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