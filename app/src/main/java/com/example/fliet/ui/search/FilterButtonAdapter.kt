package com.example.fliet.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R
import com.example.fliet.databinding.ItemFilterButtonBinding

data class FilterButton(
    val text: String,
    val iconRes: Int? = null,
    val hasIcon: Boolean = false
)

class FilterButtonAdapter(
    private val filterButtons: List<FilterButton>,
    private val onFilterClick: (Int) -> Unit
) : RecyclerView.Adapter<FilterButtonAdapter.ViewHolder>() {

    private var selectedPosition = 0

    class ViewHolder(val binding: ItemFilterButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        val container: LinearLayout = binding.clFilterItem
        val icon: ImageView = binding.ivFilterIcon
        val text: TextView = binding.tvFilterText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterButtonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filterButton = filterButtons[position]
        
        holder.text.text = filterButton.text
        
        if (filterButton.hasIcon && filterButton.iconRes != null) {
            holder.icon.visibility = View.VISIBLE
            holder.icon.setImageResource(filterButton.iconRes)
        } else {
            holder.icon.visibility = View.GONE
        }
        
        if (position == selectedPosition) {
            holder.container.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_filter_selected_new)
            holder.text.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.orange_header))
            holder.text.setTypeface(null, android.graphics.Typeface.BOLD)
            if (filterButton.hasIcon) {
                holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.orange_header))
            }
        } else {
            holder.container.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_filter_unselected)
            holder.text.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.primary_blue))
            holder.text.setTypeface(null, android.graphics.Typeface.NORMAL)
            if (filterButton.hasIcon) {
                holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.primary_blue))
            }
        }
        
        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(position)
            onFilterClick(position)
        }
    }

    override fun getItemCount() = filterButtons.size
    
    fun setSelectedPosition(position: Int) {
        val previousSelected = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelected)
        notifyItemChanged(position)
    }
}

