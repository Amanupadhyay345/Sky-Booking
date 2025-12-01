package com.example.fliet.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.fliet.R
import com.example.fliet.data.model.Flight
import com.example.fliet.databinding.ItemFlightSearchBinding

class FlightSearchAdapter(
    private val flights: List<Flight>,
    private val onFlightClick: (Flight) -> Unit
) : RecyclerView.Adapter<FlightSearchAdapter.ViewHolder>() {

    private var selectedPosition = 0

    class ViewHolder(val binding: ItemFlightSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgAirlineLogo: ImageView = binding.imgAirlineLogo
        val tvAirlineName: TextView = binding.tvAirlineName
        val tvFlightNumber: TextView = binding.tvFlightNumber
        val tvPrice: TextView = binding.tvPrice
        val tvMeal: TextView = binding.tvMeal
        val tvDepartureTime: TextView = binding.tvDepartureTime
        val tvArrivalTime: TextView = binding.tvArrivalTime
        val tvDuration: TextView = binding.tvDuration
        val tvStops: TextView = binding.tvStops
        val tvDetails: TextView = binding.tvDetails
        val cardView: androidx.constraintlayout.widget.ConstraintLayout = binding.cardFlightItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFlightSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight = flights[position]

        holder.tvDuration.text = "5h 0m"
        holder.tvDuration.visibility= View.VISIBLE
        
        val airlineName = flight.segments?.firstOrNull()?.airlineName 
            ?: flight.airlineCode 
            ?: "IndiGo"
        holder.tvAirlineName.text = airlineName
        
        val flightNumber = flight.segments?.firstOrNull()?.flightNumber 
            ?: "${flight.airlineCode}-000"
        holder.tvFlightNumber.text = flightNumber
        
        loadAirlineLogo(holder.imgAirlineLogo, flight)
        
        val price = flight.fares?.firstOrNull()?.totalFare?.totalAmount?.let {
            "₹ ${String.format("%.0f", it)}"
        } ?: "₹ 9,800"
        holder.tvPrice.text = price
        
        val mealInfo = flight.fares?.firstOrNull()?.foodOnBoard 
            ?: if (flight.fares?.firstOrNull()?.foodOnBoard.isNullOrEmpty()) "Paid Meal" else "Paid Meal"
        holder.tvMeal.text = mealInfo
        
        val departureTime = flight.segments?.firstOrNull()?.departureTime ?: "12:00"
        val arrivalTime = flight.segments?.lastOrNull()?.arrivalTime ?: "17:00"
        holder.tvDepartureTime.text = departureTime
        holder.tvArrivalTime.text = arrivalTime
        
        val stopsText = when {
            flight.stops == null -> "1 Stop"
            flight.stops == 0 -> "Non-stop"
            flight.stops == 1 -> "1 Stop"
            else -> "${flight.stops} Stops"
        }
        holder.tvStops.text = stopsText
        
        val duration = if (!flight.totalDuration.isNullOrEmpty()) {
            flight.totalDuration
        } else {
            calculateDuration(departureTime, arrivalTime)
        }
        holder.tvDuration.text = duration
        holder.tvDuration.visibility = View.VISIBLE
        
        val frameLayout = holder.binding.root as? android.widget.FrameLayout
        if (frameLayout != null) {
            val isSelected = selectedPosition == position
            val marginDp = if (isSelected) 3 else 2
            val marginPx = (marginDp * holder.binding.root.context.resources.displayMetrics.density).toInt()
            
            if (isSelected) {
                frameLayout.setBackgroundResource(R.drawable.bg_flight_selected)
            } else {
                frameLayout.setBackgroundResource(R.drawable.bg_flight_unselected)
            }
            
            val params = holder.cardView.layoutParams as? android.view.ViewGroup.MarginLayoutParams
            params?.setMargins(marginPx, marginPx, marginPx, marginPx)
            holder.cardView.layoutParams = params
        }
        
        holder.binding.root.setOnClickListener {
            if (selectedPosition != position) {
                val previousSelected = selectedPosition
                selectedPosition = position
                if (previousSelected != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelected)
                }
                notifyItemChanged(position)
                onFlightClick(flight)
            }
        }
        
        holder.tvDetails.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(position)
            onFlightClick(flight)
        }
    }

    override fun getItemCount() = flights.size
    
    private fun loadAirlineLogo(imageView: ImageView, flight: Flight) {
        val airlineCode = flight.airlineCode?.uppercase()
        
        if (!flight.airlineLogo.isNullOrEmpty() && flight.airlineLogo.startsWith("http")) {
            Glide.with(imageView.context)
                .load(flight.airlineLogo)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .centerInside()
                )
                .into(imageView)
            
            imageView.clearColorFilter()
        } else {
            setDefaultLogo(imageView, airlineCode)
        }
    }
    
    private fun setDefaultLogo(imageView: ImageView, airlineCode: String?) {
        when (airlineCode) {
            "6E", "INDIGO" -> {
                imageView.setImageResource(R.drawable.aeroplane)
                imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.primary_blue))
            }
            "AI", "AIC" -> {
                imageView.setImageResource(R.drawable.aeroplane)
                imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.primary_blue))
            }
            else -> {
                imageView.setImageResource(R.drawable.aeroplane)
                imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.primary_blue))
            }
        }
    }
    
    private fun calculateDuration(departureTime: String, arrivalTime: String): String {
        return try {
            val departureParts = departureTime.split(":")
            val arrivalParts = arrivalTime.split(":")
            
            if (departureParts.size == 2 && arrivalParts.size == 2) {
                val depHour = departureParts[0].toInt()
                val depMin = departureParts[1].toInt()
                val arrHour = arrivalParts[0].toInt()
                val arrMin = arrivalParts[1].toInt()
                
                var totalMinutes = (arrHour * 60 + arrMin) - (depHour * 60 + depMin)
                
                if (totalMinutes < 0) {
                    totalMinutes += 24 * 60
                }
                
                val hours = totalMinutes / 60
                val minutes = totalMinutes % 60
                
                if (minutes > 0) {
                    "${hours}h ${minutes}m"
                } else {
                    "${hours}h"
                }
            } else {
                "5h 0m"
            }
        } catch (e: Exception) {
            "5h 0m"
        }
    }
}
