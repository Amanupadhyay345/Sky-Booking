package com.example.fliet.data.model

import com.google.gson.annotations.SerializedName

data class FlightSearchRequest(
    @SerializedName("adultCount")
    val adultCount: String,
    @SerializedName("childCount")
    val childCount: String,
    @SerializedName("infantCount")
    val infantCount: String,
    @SerializedName("travelClass")
    val travelClass: String,
    @SerializedName("travelType")
    val travelType: String,
    @SerializedName("bookingType")
    val bookingType: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("tripInfo")
    val tripInfo: List<TripInfo>
)

data class TripInfo(
    @SerializedName("origin")
    val origin: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("travelDate")
    val travelDate: String
)

