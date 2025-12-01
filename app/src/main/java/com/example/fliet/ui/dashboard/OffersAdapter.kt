package com.example.fliet.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R

data class Offer(val imageRes: Int)

class OffersAdapter(
    private val offers: List<Offer>,
    private val onOfferClick: (Offer) -> Unit = {}
) : RecyclerView.Adapter<OffersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_offer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_offer, parent, false)
        
        val screenWidth = parent.context.resources.displayMetrics.widthPixels
        val paddingDp = 48f
        val paddingPx = (paddingDp * parent.context.resources.displayMetrics.density).toInt()
        val cardWidth = screenWidth - paddingPx
        
        val params = view.layoutParams as? RecyclerView.LayoutParams ?: RecyclerView.LayoutParams(
            cardWidth,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.width = cardWidth
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        view.layoutParams = params
        
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offer = offers[position]
        
        try {
            holder.imageView.setImageResource(offer.imageRes)
            holder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } catch (e: Exception) {
            e.printStackTrace()
            holder.imageView.setImageDrawable(null)
            holder.imageView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.primary_gradient)
            )
        }
        
        holder.itemView.setOnClickListener {
            onOfferClick(offer)
        }
    }

    override fun getItemCount() = offers.size
}
