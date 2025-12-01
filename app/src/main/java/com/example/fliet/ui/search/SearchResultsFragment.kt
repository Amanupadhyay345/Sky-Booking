package com.example.fliet.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.DashboardActivity
import com.example.fliet.R
import com.example.fliet.databinding.FragmentSearchResultsBinding
import com.example.fliet.data.model.FlightSearchResponse
import com.example.fliet.ui.home.FlightSearchViewModel

class SearchResultsFragment : Fragment() {

    private val viewModel: FlightSearchViewModel by activityViewModels()

    private var selectedFlightPrice: String? = null
    private var selectedDatePosition = 0
    private var selectedFilterPosition = 0
    private var isOnwardSelected = true
    // Don't store flightSearchResponse as field - get from ViewModel when needed to avoid TransactionTooLargeException
    private var flightSearchAdapterLeft: FlightSearchAdapter? = null
    private var flightSearchAdapterRight: FlightSearchAdapter? = null
    private var recyclerViewLeft: RecyclerView? = null
    private var recyclerViewRight: RecyclerView? = null
    private var onBackPressedCallback: OnBackPressedCallback? = null
    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!
    
    // Helper to get response from ViewModel without storing it
    private fun getFlightSearchResponse(): FlightSearchResponse? {
        return viewModel.lastSearchResponse
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigation()
        
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBottomNavigation()
                try {
                    findNavController().popBackStack(R.id.homeFragment, false)
                } catch (e: Exception) {
                    try {
                        findNavController().popBackStack()
                    } catch (ex: Exception) {
                        android.util.Log.e("SearchResults", "Error navigating back: ${ex.message}", ex)
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback!!)

        try {
            binding.btnBack.setOnClickListener {
                var intent = Intent(requireContext(), DashboardActivity::class.java)
                startActivity(intent)
            }

            val searchParams = viewModel.searchParams
            val tripType = searchParams?.tripType ?: "one_way"
            val from = searchParams?.from ?: "Mumbai"
            val to = searchParams?.to ?: "New Delhi"
            val date = searchParams?.date ?: "Wed, 18 Jun"
            val returnDate = searchParams?.returnDate
            val passengers = searchParams?.passengers ?: "1"
            
            // Don't store in field - get from ViewModel when needed to avoid TransactionTooLargeException

            binding.tvFromCity.text = extractCityName(from)
            binding.tvToCity.text = extractCityName(to)
            
            val detailsText = "$date • $passengers Traveller • Economy"
            binding.tvDetails.text = detailsText
            
            binding.tvTotalPrice.text = "₹ 9,800"
            binding.tvDiscountOffer.text = "Get 20% Off"

            setupDateSelection(binding.root)

            setupFilterButtonsRecyclerView(binding.root)

            setupFlightDirection(binding.root)

            try {
                recyclerViewLeft = binding.rvListLeft
                recyclerViewRight = binding.rvListRight
                if (recyclerViewLeft != null && recyclerViewRight != null && context != null) {
                    setupFlightRecyclerView()
                } else {
                    android.util.Log.e("SearchResults", "RecyclerView or context is null! Left: ${recyclerViewLeft != null}, Right: ${recyclerViewRight != null}, Context: ${context != null}")
                }
            } catch (e: Exception) {
                android.util.Log.e("SearchResults", "Error setting up RecyclerViews: ${e.message}", e)
                e.printStackTrace()
            }
            
            binding.btnContinue.setOnClickListener {
                android.widget.Toast.makeText(requireContext(), "Next phase available soon.", android.widget.Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            android.util.Log.e("SearchResults", "Error in onViewCreated", e)
            e.printStackTrace()
        }
    }

    private fun extractCityName(locationText: String): String {
        return locationText.split("(").first().trim()
    }

    private fun setupDateSelection(view: View) {
        binding.llDates.removeAllViews()
        
        val datesData = getDatesData()
        
        val dateLayouts = mutableListOf<android.widget.LinearLayout>()
        
        datesData.forEachIndexed { index, dateInfo ->
            val dateLayout = createDateItemLayout(dateInfo, index)
            binding.llDates.addView(dateLayout)
            dateLayouts.add(dateLayout)
            
            dateLayout.setOnClickListener {
                selectedDatePosition = index
                updateDateSelection(dateLayouts)
            }
        }
        
        updateDateSelection(dateLayouts)
    }
    
    private fun getDatesData(): List<Pair<String, String>> {
        val calendar = java.util.Calendar.getInstance()
        val dateFormat = java.text.SimpleDateFormat("EEE, d MMM", java.util.Locale.ENGLISH)
        val dates = mutableListOf<Pair<String, String>>()
        
        for (i in 0..6) {
            calendar.add(java.util.Calendar.DAY_OF_MONTH, i)
            val dateStr = dateFormat.format(calendar.time)
            val price = "₹6,175"
            dates.add(Pair(dateStr, price))
            calendar.add(java.util.Calendar.DAY_OF_MONTH, -i)
        }
        
        return dates
    }
    
    private fun createDateItemLayout(dateInfo: Pair<String, String>, index: Int): android.widget.LinearLayout {
        val layout = android.widget.LinearLayout(requireContext()).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setPadding(
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 4,
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 4,
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 4,
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 4
            )
            isClickable = true
            isFocusable = true
        }
        
        val params = android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layout.layoutParams = params
        
        val dateTextView = TextView(requireContext()).apply {
            text = dateInfo.first
            textSize = 14f
            setPadding(0, 0, 0, resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 8)
        }
        
        val priceTextView = TextView(requireContext()).apply {
            text = dateInfo.second
            textSize = 12f
        }
        
        val dividerContainer = android.widget.RelativeLayout(requireContext()).apply {
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 8
            }
            visibility = View.GONE
        }
        
        val orangeBar = View(requireContext()).apply {
            layoutParams = android.widget.RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 8
            ).apply {
                addRule(android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange_header))
        }
        
        val triangleView = android.widget.ImageView(requireContext()).apply {
            layoutParams = android.widget.RelativeLayout.LayoutParams(
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 2,
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 4
            ).apply {
                addRule(android.widget.RelativeLayout.CENTER_HORIZONTAL)
                addRule(android.widget.RelativeLayout.ALIGN_PARENT_TOP)
            }
            setImageResource(R.drawable.ic_triangle_up)
        }
        
        dividerContainer.addView(orangeBar)
        dividerContainer.addView(triangleView)
        
        layout.addView(dateTextView)
        layout.addView(priceTextView)
        layout.addView(dividerContainer)
        
        return layout
    }

    private fun updateDateSelection(dateLayouts: List<android.widget.LinearLayout>) {
        dateLayouts.forEachIndexed { index, dateLayout ->
            dateLayout.let {
                val dateTextView = it.getChildAt(0) as? TextView
                val priceTextView = it.getChildAt(1) as? TextView
                val dividerContainer = it.getChildAt(2) as? android.widget.RelativeLayout
                
                if (index == selectedDatePosition) {
                    dateTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange_header))
                    dateTextView?.setTypeface(null, android.graphics.Typeface.BOLD)
                    priceTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange_header))
                    priceTextView?.setTypeface(null, android.graphics.Typeface.BOLD)
                    dividerContainer?.visibility = View.VISIBLE
                } else {
                    dateTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                    dateTextView?.setTypeface(null, android.graphics.Typeface.NORMAL)
                    priceTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                    priceTextView?.setTypeface(null, android.graphics.Typeface.NORMAL)
                    dividerContainer?.visibility = View.GONE
                }
            }
        }
    }

    private fun android.view.ViewGroup.getChildViews(): List<View> {
        val views = mutableListOf<View>()
        for (i in 0 until childCount) {
            views.add(getChildAt(i))
        }
        return views
    }

    private fun setupFilterButtonsRecyclerView(view: View) {
        val filterButtons = listOf(
            FilterButton("Price", null, false),
            FilterButton("Sort", R.drawable.ic_sort, true),
            FilterButton("Filter", R.drawable.ic_filter, true),
            FilterButton("Departure", null, false),
            FilterButton("Arrival", null, false),
            FilterButton("Duration", null, false)
        )
        
        val adapter = FilterButtonAdapter(filterButtons) { position ->
            selectedFilterPosition = position
        }
        
        binding.rvFilterButtons.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvFilterButtons.adapter = adapter
        
        adapter.setSelectedPosition(selectedFilterPosition)
    }

    private fun setupFlightDirection(view: View) {
        binding.cardOnward.setOnClickListener {
            if (!isOnwardSelected) {
                isOnwardSelected = true
                showLoader()
                updateFlightDirection()
            }
        }

        binding.cardReturn.setOnClickListener {
            if (isOnwardSelected) {
                isOnwardSelected = false
                showLoader()
                updateFlightDirection()
            }
        }

        updateFlightDirection()
    }
    
    private fun showLoader() {
        binding.progressLoader.visibility = android.view.View.VISIBLE
    }
    
    private fun hideLoader() {
        binding.progressLoader.visibility = android.view.View.GONE
    }

    private fun updateFlightDirection() {

        if (isOnwardSelected) {
            binding.cardOnward.setBackgroundResource(R.drawable.bg_tab_selected_left)
            binding.cardReturn.setBackgroundResource(R.drawable.bg_tab_unselected_right)
            
            binding.tvOnwardRoute.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvOnwardDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvReturnRoute.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
            binding.tvReturnDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
        } else {
            binding.cardOnward.setBackgroundResource(R.drawable.bg_tab_unselected_left)
            binding.cardReturn.setBackgroundResource(R.drawable.bg_tab_selected_right)
            
            binding.tvOnwardRoute.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
            binding.tvOnwardDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
            binding.tvReturnRoute.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.tvReturnDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        
        updateFlightsForSelectedTab()
    }
    
    private fun getFlightsForSelectedTab(): List<com.example.fliet.data.model.Flight> {
        // Get from ViewModel instead of storing in field to avoid TransactionTooLargeException
        val flightSearchResponse = getFlightSearchResponse()
        val tripDetails = flightSearchResponse?.data?.tripDetails
        
        return if (tripDetails != null && tripDetails.isNotEmpty()) {
            val index = if (isOnwardSelected) 0 else 1
            
            if (index < tripDetails.size) {
                tripDetails[index].flights ?: emptyList()
            } else {
                tripDetails[0].flights ?: emptyList()
            }
        } else {
            emptyList()
        }
    }
    
    private fun setupFlightRecyclerView() {
        try {
            val flights = getFlightsForSelectedTab()
            
            val flightList = if (flights.isNotEmpty()) {
                flights
            } else {
                getMockFlights()
            }
            
            // Split flight list into two halves - no duplicate data
            val midPoint = (flightList.size + 1) / 2
            val leftFlightList = flightList.take(midPoint)
            val rightFlightList = flightList.drop(midPoint)
            
            // Setup Left RecyclerView with LinearLayoutManager (Vertical)
            if (recyclerViewLeft != null && context != null) {
                val leftLayoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerViewLeft?.layoutManager = leftLayoutManager
        
        flightSearchAdapterLeft = FlightSearchAdapter(leftFlightList) { flight ->
            val price = flight.fares?.firstOrNull()?.totalFare?.totalAmount?.let {
                "₹ ${String.format("%.0f", it)}"
            } ?: "₹ 9,800"
            selectedFlightPrice = price
            binding.tvTotalPrice.text = price
            binding.tvDiscountOffer.text = "Get 20% Off"
        }
        
                recyclerViewLeft?.adapter = flightSearchAdapterLeft
                recyclerViewLeft?.visibility = View.VISIBLE
                recyclerViewLeft?.setHasFixedSize(false)
            }
            
            // Setup Right RecyclerView with LinearLayoutManager (Vertical)
            if (recyclerViewRight != null && context != null) {
                val rightLayoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerViewRight?.layoutManager = rightLayoutManager
        
        flightSearchAdapterRight = FlightSearchAdapter(rightFlightList) { flight ->
            val price = flight.fares?.firstOrNull()?.totalFare?.totalAmount?.let {
                "₹ ${String.format("%.0f", it)}"
            } ?: "₹ 9,800"
            selectedFlightPrice = price
            binding.tvTotalPrice.text = price
            binding.tvDiscountOffer.text = "Get 20% Off"
        }
        
                recyclerViewRight?.adapter = flightSearchAdapterRight
                recyclerViewRight?.visibility = View.VISIBLE
                recyclerViewRight?.setHasFixedSize(false)
            }
            
            flightSearchAdapterLeft?.notifyDataSetChanged()
            flightSearchAdapterRight?.notifyDataSetChanged()
            
            // Set initial price from first flight (prefer left, then right)
            val firstFlight = leftFlightList.firstOrNull() ?: rightFlightList.firstOrNull()
            if (firstFlight != null) {
                val price = firstFlight.fares?.firstOrNull()?.totalFare?.totalAmount?.let {
                    "₹ ${String.format("%.0f", it)}"
                } ?: "₹ 9,800"
                selectedFlightPrice = price
                binding.tvTotalPrice.text = price
                binding.tvDiscountOffer.text = "Get 20% Off"
            }
        } catch (e: Exception) {
            android.util.Log.e("SearchResults", "Error in setupFlightRecyclerView: ${e.message}", e)
            e.printStackTrace()
        }
    }
    
    private fun updateFlightsForSelectedTab() {
        binding.root.postDelayed({
            try {
                val flights = getFlightsForSelectedTab()
                
                val flightList = if (flights.isNotEmpty()) {
                    android.util.Log.d("SearchResults", "Updating flights for ${if (isOnwardSelected) "Onward" else "Return"}: ${flights.size} flights")
                    flights
                } else {
                    android.util.Log.w("SearchResults", "No flights from API, using mock data")
                    getMockFlights()
                }
                
                // Split flight list into two halves - no duplicate data
                val midPoint = (flightList.size + 1) / 2
                val leftFlightList = flightList.take(midPoint)
                val rightFlightList = flightList.drop(midPoint)
                
                // Update Left RecyclerView
                if (recyclerViewLeft != null && context != null) {
                    flightSearchAdapterLeft = FlightSearchAdapter(leftFlightList) { flight ->
                val price = flight.fares?.firstOrNull()?.totalFare?.totalAmount?.let {
                    "₹ ${String.format("%.0f", it)}"
                } ?: "₹ 9,800"
                selectedFlightPrice = price
                binding.tvTotalPrice.text = price
                binding.tvDiscountOffer.text = "Get 20% Off"
            }
            
                    recyclerViewLeft?.adapter = flightSearchAdapterLeft
                    flightSearchAdapterLeft?.notifyDataSetChanged()
                }
                
                // Update Right RecyclerView
                if (recyclerViewRight != null && context != null) {
                    flightSearchAdapterRight = FlightSearchAdapter(rightFlightList) { flight ->
                val price = flight.fares?.firstOrNull()?.totalFare?.totalAmount?.let {
                    "₹ ${String.format("%.0f", it)}"
                } ?: "₹ 9,800"
                selectedFlightPrice = price
                binding.tvTotalPrice.text = price
                binding.tvDiscountOffer.text = "Get 20% Off"
            }
            
                    recyclerViewRight?.adapter = flightSearchAdapterRight
                    flightSearchAdapterRight?.notifyDataSetChanged()
                }
                
                // Set initial price from first flight (prefer left, then right)
                val firstFlight = leftFlightList.firstOrNull() ?: rightFlightList.firstOrNull()
                if (firstFlight != null) {
                    val price = firstFlight.fares?.firstOrNull()?.totalFare?.totalAmount?.let {
                        "₹ ${String.format("%.0f", it)}"
                    } ?: "₹ 9,800"
                    selectedFlightPrice = price
                    binding.tvTotalPrice.text = price
                    binding.tvDiscountOffer.text = "Get 20% Off"
                }
                
                hideLoader()
            } catch (e: Exception) {
                android.util.Log.e("SearchResults", "Error in updateFlightsForSelectedTab: ${e.message}", e)
                e.printStackTrace()
                hideLoader()
            }
        }, 300)
    }
    
    private fun getMockFlights(): List<com.example.fliet.data.model.Flight> {
        val mockSegment = com.example.fliet.data.model.Segment(
            aircraftType = null,
            airlineCode = "6E",
            airlineName = "IndiGo",
            arrivalAirport = "DEL",
            arrivalCity = "Delhi",
            arrivalDate = null,
            arrivalTime = "17:00",
            departureAirport = "BOM",
            departureCity = "Mumbai",
            departureDate = null,
            departureTime = "12:00",
            duration = "5h 0m",
            durationInMinutes = 300,
            flightNumber = "6E-0402",
            segmentId = 0
        )
        val mockTotalFare = com.example.fliet.data.model.TotalFare(
            airportTaxAmount = 1262.0,
            basicAmount = 9256.0,
            currencyCode = "INR",
            promoDiscount = 0.0,
            serviceFeeAmount = 0.0,
            totalAmount = 9800.0,
            tradeMarkupAmount = 0.0,
            yqAmount = 0.0
        )
        val mockFare = com.example.fliet.data.model.Fare(
            fareDetails = null,
            fareType = 0,
            fareId = "mock-fare-id",
            fareKey = null,
            foodOnBoard = "Paid Meal",
            gstMandatory = false,
            lastFewSeats = null,
            productClass = "L",
            promptMessage = null,
            refundable = true,
            seatsAvailable = "3",
            warning = null,
            totalFare = mockTotalFare
        )
        return listOf(
            com.example.fliet.data.model.Flight(
                airlineCode = "6E",
                airlineLogo = null,
                blockTicketAllowed = true,
                cached = false,
                destination = "DEL",
                fares = listOf(mockFare),
                origin = "BOM",
                segments = listOf(mockSegment),
                stops = 0,
                totalDuration = "5h 0m",
                totalDurationInMinutes = 300
            ),
            com.example.fliet.data.model.Flight(
                airlineCode = "6E",
                airlineLogo = null,
                blockTicketAllowed = true,
                cached = false,
                destination = "DEL",
                fares = listOf(mockFare),
                origin = "BOM",
                segments = listOf(mockSegment),
                stops = 0,
                totalDuration = "5h 0m",
                totalDurationInMinutes = 300
            ),
            com.example.fliet.data.model.Flight(
                airlineCode = "6E",
                airlineLogo = null,
                blockTicketAllowed = true,
                cached = false,
                destination = "DEL",
                fares = listOf(mockFare),
                origin = "BOM",
                segments = listOf(mockSegment),
                stops = 0,
                totalDuration = "5h 0m",
                totalDurationInMinutes = 300
            )
        )
    }
    
    private fun hideBottomNavigation() {
        activity?.let {
            if (it is DashboardActivity) {
                it.hideBottomNavigation()
            }
        }
    }
    
    private fun showBottomNavigation() {
        activity?.let {
            if (it is DashboardActivity) {
                it.showBottomNavigation()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback?.remove()
        onBackPressedCallback = null
        showBottomNavigation()
        _binding = null
    }
}
