package com.example.fliet.data.api

import com.example.fliet.data.model.FlightSearchRequest
import com.example.fliet.data.model.FlightSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FlightApiService {
    @POST("api/v1/app/flight/search")
    suspend fun searchFlights(
        @Body request: FlightSearchRequest
    ): Response<FlightSearchResponse>
}

