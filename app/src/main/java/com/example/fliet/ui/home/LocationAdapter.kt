package com.example.fliet.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R

data class Location(
    val code: String,
    val name: String,
    val fullName: String
)

class LocationAdapter(
    private var locations: List<Location>,
    private val onLocationClick: (Location) -> Unit
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLocationName: TextView = view.findViewById(R.id.tv_location_name)
        val tvLocationFull: TextView = view.findViewById(R.id.tv_location_full)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.tvLocationName.text = location.name
        holder.tvLocationFull.text = location.fullName
        
        holder.itemView.setOnClickListener(null)
        
        holder.itemView.setOnClickListener {
            onLocationClick(location)
        }
        
        holder.itemView.isClickable = true
        holder.itemView.isFocusable = true
    }

    override fun getItemCount() = locations.size
    
    fun updateLocations(newLocations: List<Location>) {
        locations = newLocations
        notifyDataSetChanged()
    }
}

