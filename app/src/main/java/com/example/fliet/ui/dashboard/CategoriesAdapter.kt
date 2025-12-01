package com.example.fliet.ui.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R

data class Category(val name: String, val iconRes: Int)

class CategoriesAdapter(
    private var categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iv_category_icon)
        val name: TextView = view.findViewById(R.id.tv_category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        
        holder.name.text = category.name
        holder.name.setTextColor(holder.itemView.context.resources.getColor(android.R.color.black, null))
        holder.name.visibility = View.VISIBLE
        
        try {
            holder.icon.setImageResource(category.iconRes)
            holder.icon.visibility = View.VISIBLE
        } catch (e: Exception) {
            Log.e("CategoriesAdapter", "Error setting icon for ${category.name}: ${e.message}")
            holder.icon.visibility = View.VISIBLE
        }
        
        holder.itemView.visibility = View.VISIBLE
        holder.itemView.alpha = 1f
        
        holder.itemView.setOnClickListener { onCategoryClick(category) }
    }
    
    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
