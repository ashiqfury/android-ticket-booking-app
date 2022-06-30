package com.example.ticketnow.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ticketnow.R
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.viewmodels.BookingDetailViewModel
import kotlinx.android.synthetic.main.fragment_booking_detail.view.*
import org.w3c.dom.Text

class BookingDetailFragment : Fragment() {

    private var movieId = 0
    private var theatreId: Int = 0
    private lateinit var viewModel: BookingDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BookingDetailViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        val bundle = this.arguments
        if (bundle != null) {
            this.movieId = bundle.getInt("movieId", 1)
            this.theatreId = bundle.getInt("theatreId", 1)
        }

        if (activity != null) {
            (activity as MainActivity).title = "Booking Details"
        }
        if ((activity as MainActivity).supportActionBar != null) {
            val actionBar = (activity as MainActivity).supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }
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
//                registerUser(view)
                Log.d("BOOK_MY_TICKET", view.et_booking_ticket_count.toString())
                /*val ticketCount = view.et_booking_ticket_count.toString() as Int
                val tickets = 2
                val userId = getUserId(view) ?: 1
                val bookingId = registerTicket(userId, tickets)
                navigateToConfirmFragment(bookingId)*/
                navigateToConfirmFragment(1)
            } else {
                Toast.makeText(requireContext(), "Please fill all required fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerTicket(userId: Int, ticketCount: Int): Int {
        viewModel.insertBooking(movieId, theatreId, userId, ticketCount)
        return getBookingId()
    }

    private fun getBookingId(): Int {
        val bookings = viewModel.getBookings()
        return bookings[bookings.size - 1].id
    }

    private fun getUserId(view: View): Int? {
        val user = viewModel.getUserByName(view.et_booking_name.text.toString())
        return user?.id
    }

    private fun registerUser(view: View) {
        val name = view.et_booking_name.text.toString()
        val number = view.et_booking_contactno.text.toString().toLong()

        Log.d("BOOK_MY_SHOW", "FRAGMENT CALLING $name $number")

        viewModel.insertUser(name, number)

    }

    private fun validateInputFields(view: View): Boolean {
        val name = view.et_booking_name.text.toString()
        val number = view.et_booking_contactno.text.toString()
        val ticketCount = view.et_booking_ticket_count.text.toString()

        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(ticketCount))
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