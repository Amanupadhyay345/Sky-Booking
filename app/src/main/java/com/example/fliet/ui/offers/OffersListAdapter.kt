package com.example.fliet.ui.offers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R
import com.example.fliet.ui.OfferItem

class OffersListAdapter(
    private val offers: List<OfferItem>
) : RecyclerView.Adapter<OffersListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivOffer: ImageView = view.findViewById(R.id.iv_offer)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvSubtitle: TextView = view.findViewById(R.id.tv_subtitle)
        val tvDiscount: TextView = view.findViewById(R.id.tv_discount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_offer_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < offers.size) {
            val offer = offers[position]
            holder.ivOffer.setImageResource(offer.image)
            holder.tvTitle.text = offer.title
            holder.tvSubtitle.text = offer.subtitle
            holder.tvDiscount.text = offer.discount
        }
    }

    override fun getItemCount() = offers.size
}

