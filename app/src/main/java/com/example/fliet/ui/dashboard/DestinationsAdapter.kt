package com.example.fliet.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R

data class Destination(
    val name: String,
    val tag: String,
    val imageRes: Int,
    val budget: String,
    val activityIconRes: Int,
    val hasSunnyWeather: Boolean,
    val hasRainyWeather: Boolean,
    var isFavorite: Boolean = false
)

class DestinationsAdapter(
    private val destinations: MutableList<Destination>,
    private val onFavoriteClick: (Destination) -> Unit
) : RecyclerView.Adapter<DestinationsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivDestination: ImageView = view.findViewById(R.id.iv_destination)
        val ivFavorite: ImageView = view.findViewById(R.id.iv_favorite)
        val tvDestinationName: TextView = view.findViewById(R.id.tv_destination_name)
        val tvDestinationTag: TextView = view.findViewById(R.id.tv_destination_tag)
        val tvBudget: TextView = view.findViewById(R.id.tv_budget)
        val ivActivity: ImageView = view.findViewById(R.id.iv_activity)
        val ivWeatherSun: ImageView = view.findViewById(R.id.iv_weather_sun)
        val ivWeatherRain: ImageView = view.findViewById(R.id.iv_weather_rain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destination, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dest = destinations[position]
        
        try {
            holder.ivDestination.setImageResource(dest.imageRes)
            holder.ivDestination.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            holder.ivDestination.visibility = View.VISIBLE
        } catch (e: Exception) {
            android.util.Log.e("DestinationsAdapter", "Error loading image: ${e.message}")
            holder.ivDestination.setImageResource(com.example.fliet.R.drawable.offer)
        }
        
        holder.tvDestinationName.text = dest.name
        holder.tvDestinationTag.text = dest.tag
        holder.tvBudget.text = dest.budget
        
        holder.ivActivity.setImageResource(dest.activityIconRes)
        
        holder.ivWeatherSun.visibility = if (dest.hasSunnyWeather) View.VISIBLE else View.GONE
        holder.ivWeatherRain.visibility = if (dest.hasRainyWeather) View.VISIBLE else View.GONE
        
        updateFavoriteIcon(holder.ivFavorite, dest.isFavorite)
        
        holder.ivFavorite.setOnClickListener {
            dest.isFavorite = !dest.isFavorite
            updateFavoriteIcon(holder.ivFavorite, dest.isFavorite)
            onFavoriteClick(dest)
        }
    }
    
    private fun updateFavoriteIcon(imageView: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            imageView.setImageResource(R.drawable.ic_heart_filled)
            imageView.clearColorFilter()
        } else {
            imageView.setImageResource(R.drawable.ic_heart_outline)
            imageView.setColorFilter(android.graphics.Color.GRAY, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    override fun getItemCount() = destinations.size
}
