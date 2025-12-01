package com.example.fliet.ui.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R
import com.example.fliet.databinding.FragmentOffersBinding
import com.example.fliet.ui.OfferItem

class OffersFragment : Fragment() {

    private var _binding: FragmentOffersBinding? = null
    private val binding get() = _binding!!

    private val offers = listOf(
        OfferItem(
            title = "Get 20% Off on Domestic Flights",
            subtitle = "Book now and save up to ₹2,000 on your next flight",
            discount = "20% OFF",
            image = R.drawable.newbanner
        ),
        OfferItem(
            title = "Special Weekend Deals",
            subtitle = "Exclusive weekend offers on all flight bookings",
            discount = "15% OFF",
            image = R.drawable.newbaner4
        ),
        OfferItem(
            title = "Student Discount Available",
            subtitle = "Show your student ID and get special discounts",
            discount = "25% OFF",
            image = R.drawable.newbanner2
        ),
        OfferItem(
            title = "Early Bird Special",
            subtitle = "Book 30 days in advance and save more",
            discount = "30% OFF",
            image = R.drawable.newbanner7
        ),
        OfferItem(
            title = "Group Booking Discount",
            subtitle = "Travel with friends and family, get group discounts",
            discount = "18% OFF",
            image = R.drawable.newbanner6
        ),
        OfferItem(
            title = "Loyalty Rewards",
            subtitle = "Earn points on every booking and redeem them",
            discount = "10% OFF",
            image = R.drawable.newbaner4
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.visibility = View.VISIBLE

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        setupOffersList()
    }

    private fun setupOffersList() {
        binding.rvOffers.layoutManager = LinearLayoutManager(requireContext())
        val adapter = OffersListAdapter(offers)
        binding.rvOffers.adapter = adapter
        binding.rvOffers.setHasFixedSize(true)
        binding.rvOffers.visibility = View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


