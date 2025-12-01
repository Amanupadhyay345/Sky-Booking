package com.example.fliet.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R
import com.example.fliet.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val notifications = listOf(
        NotificationItem(
            title = "Flight Booking Confirmed",
            subtitle = "Your flight from Mumbai to New Delhi has been confirmed",
            icon = R.drawable.aeroplane,
            time = "2 hours ago"
        ),
        NotificationItem(
            title = "Special Offer Available",
            subtitle = "Get 20% off on your next flight booking",
            icon = R.drawable.gift,
            time = "5 hours ago"
        ),
        NotificationItem(
            title = "Payment Successful",
            subtitle = "Your payment of ₹9,800 has been processed successfully",
            icon = R.drawable.wallet,
            time = "1 day ago"
        ),
        NotificationItem(
            title = "Check-in Reminder",
            subtitle = "Don't forget to check-in for your flight tomorrow",
            icon = R.drawable.ticket,
            time = "2 days ago"
        ),
        NotificationItem(
            title = "New Destination Added",
            subtitle = "Explore new destinations with exclusive offers",
            icon = R.drawable.aeroplane,
            time = "3 days ago"
        ),
        NotificationItem(
            title = "Booking Cancelled",
            subtitle = "Your booking has been cancelled and refund initiated",
            icon = R.drawable.ticket,
            time = "1 week ago"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.visibility = View.VISIBLE

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (context != null) {
            val layoutManager = LinearLayoutManager(context)
            binding.rvNotifications.layoutManager = layoutManager
            val adapter = NotificationAdapter(notifications)
            binding.rvNotifications.adapter = adapter
            binding.rvNotifications.setHasFixedSize(true)
            binding.rvNotifications.visibility = View.VISIBLE
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class NotificationItem(
    val title: String,
    val subtitle: String,
    val icon: Int,
    val time: String
)

