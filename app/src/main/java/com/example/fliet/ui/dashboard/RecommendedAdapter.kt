package com.example.fliet.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R

data class Recommendation(
    val title: String, 
    val value: String, 
    val description: String, 
    val backgroundRes: Int
)

class RecommendedAdapter(
    private val items: List<Recommendation>,
    private val onBookNowClick: (Recommendation) -> Unit
) : RecyclerView.Adapter<RecommendedAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: ConstraintLayout = view.findViewById(R.id.cl_recommended_container)
        val title: TextView = view.findViewById(R.id.tv_rec_title)
        val value: TextView = view.findViewById(R.id.tv_rec_value)
        val desc: TextView = view.findViewById(R.id.tv_rec_desc)
        val btnBookNow: Button = view.findViewById(R.id.btn_book_now)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.value.text = item.value
        holder.desc.text = item.description
        
        holder.container.background = ContextCompat.getDrawable(holder.itemView.context, item.backgroundRes)
        
        holder.btnBookNow.setOnClickListener {
            onBookNowClick(item)
        }
    }

    override fun getItemCount() = items.size
}
