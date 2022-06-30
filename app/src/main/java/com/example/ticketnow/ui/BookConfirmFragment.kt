package com.example.ticketnow.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.ticketnow.R
import com.example.ticketnow.data.models.BookTicketModel
import com.example.ticketnow.data.models.MovieModel
import com.example.ticketnow.data.models.TheatreModel
import com.example.ticketnow.data.models.UserModel
import com.example.ticketnow.viewmodels.BookingConfirmViewModel
import com.example.ticketnow.viewmodels.BookingDetailViewModel
import kotlinx.android.synthetic.main.fragment_book_confirm.view.*

class BookConfirmFragment : Fragment() {

    private var bookingId = 0
    private lateinit var viewModel: BookingConfirmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BookingConfirmViewModel::class.java]
        viewModel.initializeRepo(requireContext())

        val bundle = this.arguments
        if (bundle != null) {
            this.bookingId = bundle.getInt("bookingId", 0)
        }

        if (activity != null) {
            (activity as MainActivity).title = "Booking Confirm"
        }
        if ((activity as MainActivity).supportActionBar != null) {
            val actionBar = (activity as MainActivity).supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(false)
            actionBar.setHomeButtonEnabled(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserFromBooking(bookingId)?.let { user ->
            assignUserValueToView(view, user)
        }

        viewModel.getMovieFromBooking(bookingId)?.let { movie ->
            viewModel.getTicketCount(bookingId)?.let { ticketCount ->
                view.tv_confirm_ticketcount.text = "Tickets: ${ticketCount}x"
                assignMovieValueToView(view, movie, ticketCount)
            }
        }
        viewModel.getTheatreFromBooking(bookingId)?.let { theatre ->
            assignTheatreValueToView(view, theatre)
        }

        // clear the backstack and go back to the home page
        view.findViewById<Button>(R.id.btn_confirm_home).setOnClickListener {
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private fun assignTheatreValueToView(view: View, theatre: TheatreModel) {
        view.tv_confirm_theatre_name.text = "Theatre: ${theatre.name}"
        view.tv_confirm_theatre_location.text = "Location: ${theatre.location}"
    }

    private fun assignMovieValueToView(view: View, movie: MovieModel, ticketCount: Int) {
        view.tv_confirm_movie_name.text = "Movie: ${movie.name}"
        view.tv_confirm_movie_language.text = "Language: ${movie.language}"
        view.tv_confirm_movie_genre.text = "Genre: ${movie.genre}"
        view.tv_confirm_movie_showtime.text = "Show time: ${movie.time}"
        view.tv_confirm_movie_price.text = "Price per ticket: ${movie.price}"
        view.tv_confirm_totalprice.text = "Total price: ${movie.price * ticketCount}/-"
    }

    private fun assignUserValueToView(view: View, user: UserModel) {
        view.tv_confirm_name.text = "Name: ${user.name}"
        view.tv_confirm_number.text = "Contact number: ${user.phoneNumber}"
    }
}