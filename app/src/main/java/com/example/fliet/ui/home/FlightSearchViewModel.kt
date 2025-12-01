package com.example.fliet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fliet.data.model.FlightSearchRequest
import com.example.fliet.data.model.FlightSearchResponse
import com.example.fliet.data.repository.FlightRepository
import kotlinx.coroutines.launch

class FlightSearchViewModel : ViewModel() {
    private val repository = FlightRepository()
    
    private val _searchResult = MutableLiveData<Result<FlightSearchResponse>>()
    val searchResult: LiveData<Result<FlightSearchResponse>> = _searchResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    var lastSearchResponse: FlightSearchResponse? = null
        private set
    
    var searchParams: SearchParams? = null
        private set
    
    data class SearchParams(
        val tripType: String,
        val from: String,
        val to: String,
        val date: String,
        val returnDate: String?,
        val passengers: String
    )
    
    fun searchFlights(request: FlightSearchRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.searchFlights(request)
                _searchResult.value = result
                result.onSuccess { response ->
                    lastSearchResponse = response
                }
            } catch (e: Exception) {
                _searchResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setSearchParams(params: SearchParams) {
        searchParams = params
    }
}

