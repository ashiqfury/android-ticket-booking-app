package com.example.ticketnow.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ticketnow.R
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.viewmodels.BookingDetailViewModel
import com.example.ticketnow.viewmodels.TheatreListViewModel
import kotlinx.android.synthetic.main.fragment_booking_detail.view.*
import org.w3c.dom.Text

class BookingDetailFragment : Fragment() {

    private var movieId = 0
    private var theatreId: Int = 0
    private lateinit var viewModel: BookingDetailViewModel
    private lateinit var theatreViewModel: TheatreListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BookingDetailViewModel::class.java]
        theatreViewModel = ViewModelProvider(this)[TheatreListViewModel::class.java]
        viewModel.initializeRepo(requireContext())
        theatreViewModel.initializeRepo(requireContext())

        val bundle = this.arguments
        if (bundle != null) {
            this.movieId = bundle.getInt("movieId", -1)
            this.theatreId = bundle.getInt("theatreId", -1)
        }

        if (activity != null) {
            (activity as MainActivity).title = "Booking Details"
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
        return inflater.inflate(R.layout.fragment_booking_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_booking_submit).setOnClickListener {
            if (validateInputFields(view)) {
                val bookingId = registerTicket(view)
                navigateToConfirmFragment(bookingId.toInt())
            }
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

    private fun registerTicket(view: View): Long {
        val userId = registerUser(view)
        val ticketCount = view.et_booking_ticket_count.text.toString().toInt()
        return viewModel.insertBooking(movieId, theatreId, userId.toInt(), ticketCount)
    }

    private fun registerUser(view: View): Long {
        val name = view.et_booking_name.text.toString()
        val number = view.et_booking_contactno.text.toString().toLong()
        return viewModel.insertUser(name, number)
    }

    private fun validateInputFields(view: View): Boolean {
        val name = view.et_booking_name.text.toString()
        val number = view.et_booking_contactno.text.toString()
        val ticketCount = view.et_booking_ticket_count.text.toString()
        var availableSeatCount: Int? = -1
        val theatres = ArrayList<TheatreModel>()

        theatreViewModel.theatres.observe(viewLifecycleOwner) { _theatres ->
            theatres.addAll(_theatres)
        }
        theatres.forEach {
            Log.d("TICKET_NOW", "validateInputFields: ${it.id} $theatreId")
        }
        availableSeatCount = theatres.find { theatre -> theatre.id == theatreId }?.availableSeats ?: -1
        Log.d("TICKET_NOW", "validateInputFields: availableSeatCount: $availableSeatCount")

        return if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(ticketCount)) {
            Toast.makeText(requireContext(), "Please fill all required fields!", Toast.LENGTH_SHORT).show()
            false
        } else if (number.length < 7 || number.length > 12) {
            Toast.makeText(requireContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show()
            false
        } else if (ticketCount.toInt() > availableSeatCount || ticketCount.toInt() <= 0) {
            Toast.makeText(requireContext(), "Invalid ticket count!", Toast.LENGTH_SHORT).show()
            false
        } else {
            val newAvailableSeats = availableSeatCount - ticketCount.toInt()
            theatreViewModel.updateAvailableSeat(newAvailableSeats, theatreId)
            true
        }

    }

    private fun navigateToConfirmFragment(id: Int) {
        val fragment = BookConfirmFragment()
        val bundle = Bundle()
        bundle.putInt("bookingId", id)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
            commit()
        }
    }
}