package com.example.fliet.ui.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.R
import com.example.fliet.databinding.FragmentHomeBinding
import com.example.fliet.data.model.FlightSearchRequest
import com.example.fliet.data.model.TripInfo
import com.example.fliet.ui.dashboard.Offer
import com.example.fliet.ui.dashboard.OffersAdapter
import com.example.fliet.utils.LocationUtils
import com.example.fliet.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var passengerCount = 1
    private lateinit var offersAdapter: OffersAdapter
    private var autoScrollHandler: Handler? = null
    private var autoScrollRunnable: Runnable? = null
    private var currentPosition = 0
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FlightSearchViewModel by activityViewModels()
    
    private var selectedDepartDate: Calendar? = null
    private var selectedReturnDate: Calendar? = null
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    
    private val allLocations = listOf(
        Location("BOM", "Mumbai (BOM)", "Mumbai, Maharashtra, India"),
        Location("DEL", "New Delhi (DEL)", "New Delhi, Delhi, India"),
        Location("BLR", "Bangalore (BLR)", "Bangalore, Karnataka, India"),
        Location("HYD", "Hyderabad (HYD)", "Hyderabad, Telangana, India"),
        Location("CCU", "Kolkata (CCU)", "Kolkata, West Bengal, India"),
        Location("MAA", "Chennai (MAA)", "Chennai, Tamil Nadu, India"),
        Location("PNQ", "Pune (PNQ)", "Pune, Maharashtra, India"),
        Location("GOI", "Goa (GOI)", "Goa, India"),
        Location("JAI", "Jaipur (JAI)", "Jaipur, Rajasthan, India"),
        Location("AMD", "Ahmedabad (AMD)", "Ahmedabad, Gujarat, India"),
        Location("COK", "Kochi (COK)", "Kochi, Kerala, India"),
        Location("TRV", "Trivandrum (TRV)", "Trivandrum, Kerala, India"),
        Location("IXC", "Chandigarh (IXC)", "Chandigarh, India"),
        Location("LKO", "Lucknow (LKO)", "Lucknow, Uttar Pradesh, India"),
        Location("VNS", "Varanasi (VNS)", "Varanasi, Uttar Pradesh, India"),
        Location("GAU", "Guwahati (GAU)", "Guwahati, Assam, India"),
        Location("IXR", "Ranchi (IXR)", "Ranchi, Jharkhand, India"),
        Location("PAT", "Patna (PAT)", "Patna, Bihar, India"),
        Location("BBI", "Bhubaneswar (BBI)", "Bhubaneswar, Odisha, India"),
        Location("IXB", "Bagdogra (IXB)", "Bagdogra, West Bengal, India"),
        Location("DED", "Dehradun (DED)", "Dehradun, Uttarakhand, India"),
        Location("JLR", "Jabalpur (JLR)", "Jabalpur, Madhya Pradesh, India"),
        Location("IDR", "Indore (IDR)", "Indore, Madhya Pradesh, India"),
        Location("NAG", "Nagpur (NAG)", "Nagpur, Maharashtra, India"),
        Location("IXE", "Mangalore (IXE)", "Mangalore, Karnataka, India"),
        Location("IXZ", "Port Blair (IXZ)", "Port Blair, Andaman and Nicobar, India"),
        Location("SXR", "Srinagar (SXR)", "Srinagar, Jammu and Kashmir, India"),
        Location("IXJ", "Jammu (IXJ)", "Jammu, Jammu and Kashmir, India"),
        Location("IXL", "Leh (IXL)", "Leh, Ladakh, India"),
        Location("DIB", "Dibrugarh (DIB)", "Dibrugarh, Assam, India"),
        Location("IXA", "Agartala (IXA)", "Agartala, Tripura, India"),
        Location("IXS", "Silchar (IXS)", "Silchar, Assam, India"),
        Location("IXI", "Lilabari (IXI)", "Lilabari, Assam, India"),
        Location("IXD", "Allahabad (IXD)", "Allahabad, Uttar Pradesh, India"),
        Location("IXU", "Aurangabad (IXU)", "Aurangabad, Maharashtra, India"),
        Location("BHO", "Bhopal (BHO)", "Bhopal, Madhya Pradesh, India"),
        Location("RPR", "Raipur (RPR)", "Raipur, Chhattisgarh, India"),
        Location("VTZ", "Vishakhapatnam (VTZ)", "Vishakhapatnam, Andhra Pradesh, India"),
        Location("TIR", "Tirupati (TIR)", "Tirupati, Andhra Pradesh, India"),
        Location("VGA", "Vijayawada (VGA)", "Vijayawada, Andhra Pradesh, India"),
        Location("RJA", "Rajahmundry (RJA)", "Rajahmundry, Andhra Pradesh, India"),
        Location("IXY", "Kandla (IXY)", "Kandla, Gujarat, India"),
        Location("BDQ", "Vadodara (BDQ)", "Vadodara, Gujarat, India"),
        Location("STV", "Surat (STV)", "Surat, Gujarat, India"),
        Location("JDH", "Jodhpur (JDH)", "Jodhpur, Rajasthan, India"),
        Location("UDR", "Udaipur (UDR)", "Udaipur, Rajasthan, India"),
        Location("JSA", "Jaisalmer (JSA)", "Jaisalmer, Rajasthan, India"),
        Location("KNU", "Kanpur (KNU)", "Kanpur, Uttar Pradesh, India"),
        Location("GAY", "Gaya (GAY)", "Gaya, Bihar, India"),
        Location("DUM", "Darbhanga (DUM)", "Darbhanga, Bihar, India"),
        Location("IXW", "Jamshedpur (IXW)", "Jamshedpur, Jharkhand, India"),
        Location("BKB", "Bikaner (BKB)", "Bikaner, Rajasthan, India"),
        Location("KQH", "Kishangarh (KQH)", "Kishangarh, Rajasthan, India"),
        Location("BEK", "Bareilly (BEK)", "Bareilly, Uttar Pradesh, India"),
        Location("GWL", "Gwalior (GWL)", "Gwalior, Madhya Pradesh, India"),
        Location("JRG", "Jharsuguda (JRG)", "Jharsuguda, Odisha, India"),
        Location("RUP", "Rupsi (RUP)", "Rupsi, Assam, India"),
        Location("SHL", "Shillong (SHL)", "Shillong, Meghalaya, India"),
        Location("AJL", "Aizawl (AJL)", "Aizawl, Mizoram, India"),
        Location("DMU", "Dimapur (DMU)", "Dimapur, Nagaland, India"),
        Location("IXT", "Pasighat (IXT)", "Pasighat, Arunachal Pradesh, India")
    )
    
    private val allOffers = listOf(
        Offer(R.drawable.offer),
        Offer(R.drawable.offer1),
        Offer(R.drawable.offer),
        Offer(R.drawable.offer1),
        Offer(R.drawable.offer),
        Offer(R.drawable.offer1)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            try {
                if (findNavController().currentDestination?.id == R.id.homeFragment) {
                    findNavController().popBackStack(R.id.dashboardFragment, false)
                } else {
                    findNavController().popBackStack()
                }
            } catch (e: Exception) {
                try {
                    findNavController().popBackStack()
                } catch (ex: Exception) {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
            }
        }

        binding.tvViewAllHome.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_homeFragment_to_offersFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        selectedDepartDate = calendar
        updateDateDisplay(binding.tvDate, calendar)
        
        val returnCalendar = Calendar.getInstance()
        returnCalendar.add(Calendar.DAY_OF_MONTH, 3)
        selectedReturnDate = returnCalendar
        updateDateDisplay(binding.tvReturnDate, returnCalendar)
        
        binding.tvDate.setOnClickListener {
            showDatePicker(selectedDepartDate ?: Calendar.getInstance()) { calendar ->
                selectedDepartDate = calendar
                updateDateDisplay(binding.tvDate, calendar)
            }
        }
        
        binding.tvReturnDate.setOnClickListener {
            showDatePicker(selectedReturnDate ?: Calendar.getInstance()) { calendar ->
                selectedReturnDate = calendar
                updateDateDisplay(binding.tvReturnDate, calendar)
            }
        }
        
        selectCategory(binding.llCategoryFlights, binding.llCategoryBus, binding.llCategoryHotels, binding.llCategoryVisa, 0)
        
        binding.llCategoryFlights.setOnClickListener {
            selectCategory(binding.llCategoryFlights, binding.llCategoryBus, binding.llCategoryHotels, binding.llCategoryVisa, 0)
        }
        
        binding.llCategoryBus.setOnClickListener {
            selectCategory(binding.llCategoryBus, binding.llCategoryFlights, binding.llCategoryHotels, binding.llCategoryVisa, 1)
        }
        
        binding.llCategoryHotels.setOnClickListener {
            selectCategory(binding.llCategoryHotels, binding.llCategoryFlights, binding.llCategoryBus, binding.llCategoryVisa, 2)
        }
        
        binding.llCategoryVisa.setOnClickListener {
            selectCategory(binding.llCategoryVisa, binding.llCategoryFlights, binding.llCategoryBus, binding.llCategoryHotels, 3)
        }

        binding.btnOneWay.setOnClickListener {
            binding.btnOneWay.isChecked = true
            binding.btnRoundTrip.isChecked = false
            binding.layoutReturnDate.visibility = View.GONE
        }

        binding.btnRoundTrip.setOnClickListener {
            binding.btnRoundTrip.isChecked = true
            binding.btnOneWay.isChecked = false
            binding.layoutReturnDate.visibility = View.VISIBLE
        }
        
        binding.btnSwap.setOnClickListener {
            val fromText = binding.etFrom.text.toString()
            val toText = binding.etTo.text.toString()
            binding.etFrom.setText(toText)
            binding.etTo.setText(fromText)
        }
        
        binding.etFrom.setOnClickListener {
            showLocationDialog(true, binding.etFrom)
        }
        
        binding.etTo.setOnClickListener {
            showLocationDialog(false, binding.etTo)
        }
        
        binding.btnDecreasePassenger.setOnClickListener {
            if (passengerCount > 1) {
                passengerCount--
                binding.tvPassengers.text = passengerCount.toString()
            }
        }
        
        binding.btnIncreasePassenger.setOnClickListener {
            passengerCount++
            binding.tvPassengers.text = passengerCount.toString()
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.btnSearch.isEnabled = !isLoading
            if (isLoading) {
                binding.btnSearch.text = ""
                binding.progressSearch.visibility = View.VISIBLE
            } else {
                binding.btnSearch.text = "Search Flights"
                binding.progressSearch.visibility = View.GONE
            }
        })
        
        viewModel.searchResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                val currentDestination = findNavController().currentDestination?.id
                if (currentDestination == R.id.searchResultsFragment) {
                    return@Observer
                }
                
                val from = binding.etFrom.text.toString()
                val to = binding.etTo.text.toString()
                val date = binding.tvDate.text.toString()
                val returnDate = if (binding.layoutReturnDate.visibility == View.VISIBLE) binding.tvReturnDate.text.toString() else null
                val passengers = binding.tvPassengers.text.toString()
                val isRoundTrip = binding.layoutReturnDate.visibility == View.VISIBLE
                val tripType = if (isRoundTrip) "round_trip" else "one_way"
                
                viewModel.setSearchParams(
                    FlightSearchViewModel.SearchParams(
                        tripType = tripType,
                        from = from,
                        to = to,
                        date = date,
                        returnDate = returnDate,
                        passengers = passengers
                    )
                )
                
                
                if (currentDestination == R.id.homeFragment) {
                    findNavController().navigate(R.id.action_homeFragment_to_searchResultsFragment)
                }
            }.onFailure { exception ->
                android.util.Log.e("HomeFragment", "API Error: ${exception.message}", exception)
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        })
        
        binding.btnSearch.setOnClickListener {

            if (!NetworkUtils.isInternetAvailable(requireContext())) {
                showNoInternetDialog()
                return@setOnClickListener
            }
            

            val fromText = binding.etFrom.text.toString()
            val toText = binding.etTo.text.toString()
            
            if (fromText.isEmpty() || toText.isEmpty()) {
                Toast.makeText(requireContext(), "Please select origin and destination", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            

            val fromCode = extractAirportCode(fromText)
            val toCode = extractAirportCode(toText)
            
            if (fromCode.isEmpty() || toCode.isEmpty()) {
                Toast.makeText(requireContext(), "Invalid location selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            

            CoroutineScope(Dispatchers.Main).launch {
                val location = LocationUtils.getLastKnownLocationSuspend(requireContext())
                val latitude = location?.latitude?.toString() ?: "26.8724818"
                val longitude = location?.longitude?.toString() ?: "81.0179627"
                

                val tripInfoList = mutableListOf<TripInfo>()
                val departDate = selectedDepartDate?.let { dateFormat.format(it.time) } ?: dateFormat.format(Calendar.getInstance().time)
                tripInfoList.add(TripInfo(fromCode, toCode, departDate))
                
                if (binding.layoutReturnDate.visibility == View.VISIBLE) {
                    val returnDate = selectedReturnDate?.let { dateFormat.format(it.time) } ?: dateFormat.format(Calendar.getInstance().time)
                    tripInfoList.add(TripInfo(toCode, fromCode, returnDate))
                }
                
                val request = FlightSearchRequest(
                    adultCount = passengerCount.toString(),
                    childCount = "0",
                    infantCount = "0",
                    travelClass = "0",
                    travelType = if (binding.layoutReturnDate.visibility == View.VISIBLE) "1" else "0",
                    bookingType = "1",
                    longitude = longitude,
                    latitude = latitude,
                    tripInfo = tripInfoList
                )
                

                viewModel.searchFlights(request)
            }
        }
        
        setupOffersSection()
    }
    
    private fun setupOffersSection() {
        offersAdapter = OffersAdapter(allOffers) { offer ->
        }
        
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvOffersHome.layoutManager = layoutManager
        binding.rvOffersHome.adapter = offersAdapter
        binding.rvOffersHome.setHasFixedSize(false)
        
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvOffersHome)
        
        startAutoScroll(binding.rvOffersHome)
    }
    
    private fun startAutoScroll(recyclerView: RecyclerView) {
        autoScrollHandler = Handler(Looper.getMainLooper())
        autoScrollRunnable = object : Runnable {
            override fun run() {
                if (::offersAdapter.isInitialized && offersAdapter.itemCount > 0) {
                    currentPosition = (currentPosition + 1) % offersAdapter.itemCount
                    recyclerView.smoothScrollToPosition(currentPosition)
                    autoScrollHandler?.postDelayed(this, 3000)
                }
            }
        }
        autoScrollHandler?.postDelayed(autoScrollRunnable!!, 3000)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler?.removeCallbacks(autoScrollRunnable!!)
        autoScrollHandler = null
        autoScrollRunnable = null
        _binding = null
    }
    
    private fun showLocationDialog(isFrom: Boolean, targetField: EditText) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_location_search)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        
        val etSearch = dialog.findViewById<EditText>(R.id.et_search_location)
        val rvLocations = dialog.findViewById<RecyclerView>(R.id.rv_locations)
        val cardView = dialog.findViewById<androidx.cardview.widget.CardView>(R.id.card_dialog)
        val dialogRoot = dialog.findViewById<android.widget.FrameLayout>(R.id.dialog_root)
        
        dialogRoot?.setOnClickListener {
            dialog.dismiss()
        }
        
        cardView?.setOnClickListener {
        }
        
        var isDismissing = false
        val adapter = LocationAdapter(allLocations) { location ->
            if (!isDismissing) {
                isDismissing = true
                targetField.setText(location.name)
                dialog.dismiss()
            }
        }
        
        val layoutManager = LinearLayoutManager(requireContext())
        rvLocations.layoutManager = layoutManager
        rvLocations.adapter = adapter
        
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                val filtered = if (query.isEmpty()) {
                    allLocations
                } else {
                    allLocations.filter {
                        it.name.lowercase().contains(query) ||
                        it.fullName.lowercase().contains(query) ||
                        it.code.lowercase().contains(query)
                    }
                }
                adapter.updateLocations(filtered)
            }
        })
        
        dialog.show()
    }
    
    private fun selectCategory(
        selectedView: View,
        view1: View,
        view2: View,
        view3: View,
        categoryIndex: Int
    ) {
        resetCategory(view1)
        resetCategory(view2)
        resetCategory(view3)
        
        try {
            val selectedGroup = selectedView as? android.view.ViewGroup
            val iconContainer = selectedGroup?.getChildAt(0) as? android.view.ViewGroup
            iconContainer?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_light_orange)
            
            val textView = selectedGroup?.getChildAt(1) as? android.widget.TextView
            textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun resetCategory(categoryView: View) {
        try {
            val categoryGroup = categoryView as? android.view.ViewGroup
            val iconContainer = categoryGroup?.getChildAt(0) as? android.view.ViewGroup
            iconContainer?.background = null
            
            val textView = categoryGroup?.getChildAt(1) as? android.widget.TextView
            textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun showDatePicker(initialDate: Calendar, onDateSelected: (Calendar) -> Unit) {
        val year = initialDate.get(Calendar.YEAR)
        val month = initialDate.get(Calendar.MONTH)
        val day = initialDate.get(Calendar.DAY_OF_MONTH)
        
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth, selectedDay)
                onDateSelected(calendar)
            },
            year,
            month,
            day
        )
        
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }
    
    private fun updateDateDisplay(textView: TextView, calendar: Calendar) {
        textView.text = displayDateFormat.format(calendar.time)
    }
    
    private fun extractAirportCode(locationText: String): String {
        val regex = "\\(([A-Z]{3})\\)".toRegex()
        return regex.find(locationText)?.groupValues?.get(1) ?: ""
    }
    
    private fun showNoInternetDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_no_internet)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        
        dialog.findViewById<android.widget.Button>(R.id.btn_ok).setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
}
