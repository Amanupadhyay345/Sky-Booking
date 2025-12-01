package com.example.fliet.data.model

import com.google.gson.annotations.SerializedName

data class FlightSearchResponse(
    @SerializedName("statusCode")
    val statusCode: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("data")
    val data: FlightSearchData?
)

data class FlightSearchData(
    @SerializedName("requestId")
    val requestId: String?,
    @SerializedName("tripDetails")
    val tripDetails: List<TripDetail>?
)

data class TripDetail(
    @SerializedName("flights")
    val flights: List<Flight>?
)

data class Flight(
    @SerializedName("airlineCode")
    val airlineCode: String?,
    @SerializedName("airlineLogo")
    val airlineLogo: String?,
    @SerializedName("blockTicketAllowed")
    val blockTicketAllowed: Boolean?,
    @SerializedName("cached")
    val cached: Boolean?,
    @SerializedName("destination")
    val destination: String?,
    @SerializedName("fares")
    val fares: List<Fare>?,
    @SerializedName("origin")
    val origin: String?,
    @SerializedName("segments")
    val segments: List<Segment>?,
    @SerializedName("stops")
    val stops: Int?,
    @SerializedName("totalDuration")
    val totalDuration: String?,
    @SerializedName("totalDurationInMinutes")
    val totalDurationInMinutes: Int?
)

data class Segment(
    @SerializedName("aircraftType")
    val aircraftType: String?,
    @SerializedName("airlineCode")
    val airlineCode: String?,
    @SerializedName("airlineName")
    val airlineName: String?,
    @SerializedName("arrivalAirport")
    val arrivalAirport: String?,
    @SerializedName("arrivalCity")
    val arrivalCity: String?,
    @SerializedName("arrivalDate")
    val arrivalDate: String?,
    @SerializedName("arrivalTime")
    val arrivalTime: String?,
    @SerializedName("departureAirport")
    val departureAirport: String?,
    @SerializedName("departureCity")
    val departureCity: String?,
    @SerializedName("departureDate")
    val departureDate: String?,
    @SerializedName("departureTime")
    val departureTime: String?,
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("durationInMinutes")
    val durationInMinutes: Int?,
    @SerializedName("flightNumber")
    val flightNumber: String?,
    @SerializedName("segmentId")
    val segmentId: Int?
)

data class Fare(
    @SerializedName("fareDetails")
    val fareDetails: List<FareDetail>?,
    @SerializedName("fareType")
    val fareType: Int?,
    @SerializedName("fareId")
    val fareId: String?,
    @SerializedName("fareKey")
    val fareKey: String?,
    @SerializedName("foodonboard")
    val foodOnBoard: String?,
    @SerializedName("GSTMandatory")
    val gstMandatory: Boolean?,
    @SerializedName("lastFewSeats")
    val lastFewSeats: String?,
    @SerializedName("productClass")
    val productClass: String?,
    @SerializedName("promptMessage")
    val promptMessage: String?,
    @SerializedName("refundable")
    val refundable: Boolean?,
    @SerializedName("seatsAvailable")
    val seatsAvailable: String?,
    @SerializedName("warning")
    val warning: String?,
    @SerializedName("totalFare")
    val totalFare: TotalFare?
)

data class FareDetail(
    @SerializedName("airportTaxAmount")
    val airportTaxAmount: Double?,
    @SerializedName("airportTaxes")
    val airportTaxes: List<AirportTax>?,
    @SerializedName("basicAmount")
    val basicAmount: Double?,
    @SerializedName("currencyCode")
    val currencyCode: String?,
    @SerializedName("fareClasses")
    val fareClasses: List<FareClass>?,
    @SerializedName("freeBaggage")
    val freeBaggage: FreeBaggage?,
    @SerializedName("GST")
    val gst: Double?,
    @SerializedName("grossCommission")
    val grossCommission: Double?,
    @SerializedName("netCommission")
    val netCommission: Double?,
    @SerializedName("PAXType")
    val paxType: Int?,
    @SerializedName("promoDiscount")
    val promoDiscount: Double?,
    @SerializedName("serviceFeeAmount")
    val serviceFeeAmount: Double?,
    @SerializedName("TDS")
    val tds: Double?,
    @SerializedName("totalAmount")
    val totalAmount: Double?,
    @SerializedName("YQAmount")
    val yqAmount: Double?
)

data class AirportTax(
    @SerializedName("taxAmount")
    val taxAmount: Double?,
    @SerializedName("taxCode")
    val taxCode: String?,
    @SerializedName("taxDesc")
    val taxDesc: String?
)

data class FareClass(
    @SerializedName("cabinClass")
    val cabinClass: String?,
    @SerializedName("classCode")
    val classCode: String?,
    @SerializedName("classDesc")
    val classDesc: String?,
    @SerializedName("fareBasis")
    val fareBasis: String?,
    @SerializedName("privileges")
    val privileges: String?,
    @SerializedName("segmentId")
    val segmentId: Int?
)

data class FreeBaggage(
    @SerializedName("checkInBaggage")
    val checkInBaggage: String?,
    @SerializedName("displayRemarks")
    val displayRemarks: String?,
    @SerializedName("handBaggage")
    val handBaggage: String?
)

data class TotalFare(
    @SerializedName("airportTaxAmount")
    val airportTaxAmount: Double?,
    @SerializedName("basicAmount")
    val basicAmount: Double?,
    @SerializedName("currencyCode")
    val currencyCode: String?,
    @SerializedName("promoDiscount")
    val promoDiscount: Double?,
    @SerializedName("serviceFeeAmount")
    val serviceFeeAmount: Double?,
    @SerializedName("totalAmount")
    val totalAmount: Double?,
    @SerializedName("tradeMarkupAmount")
    val tradeMarkupAmount: Double?,
    @SerializedName("YQAmount")
    val yqAmount: Double?
)
