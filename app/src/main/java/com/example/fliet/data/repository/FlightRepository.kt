package com.example.fliet.data.repository

import android.util.Log
import com.example.fliet.data.api.ApiClient
import com.example.fliet.data.model.FlightSearchRequest
import com.example.fliet.data.model.FlightSearchResponse

class FlightRepository {
    private val apiService = ApiClient.flightApiService
    private val TAG = "FlightRepository"
    
    suspend fun searchFlights(request: FlightSearchRequest): Result<FlightSearchResponse> {
        return try {
            val response = apiService.searchFlights(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Log.e(TAG, "API Error: ${response.message()}, Code: ${response.code()}")
                Result.failure(Exception(response.message() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}

