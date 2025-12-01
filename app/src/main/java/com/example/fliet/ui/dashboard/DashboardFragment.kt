package com.example.fliet.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fliet.DashboardActivity
import com.example.fliet.R
import com.example.fliet.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private var isExpanded = false
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var offersAdapter: OffersAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var destinationsAdapter: DestinationsAdapter
    private var autoScrollHandler: Handler? = null
    private var autoScrollRunnable: Runnable? = null
    private var currentPosition = 0
    private var selectedTab = 0
    
    private val allCategories = listOf(
        Category("Flights", R.drawable.aeroplane),
        Category("Bus", R.drawable.bus),
        Category("Hotels", R.drawable.booking),
        Category("Visa", R.drawable.ticket),
        Category("Recharges", R.drawable.mynaui_mobile),
        Category("Utility", R.drawable.g),
        Category("Giftcard", R.drawable.gift),
        Category("View All", R.drawable.grid)
    )
    
    private val allOffers = listOf(
        Offer(R.drawable.newbanner),
        Offer(R.drawable.newbaner4),
        Offer(R.drawable.newbanner2),
        Offer(R.drawable.newbanner7),
        Offer(R.drawable.newbanner6),
        Offer(R.drawable.newbaner4)
    )
    
    private val flightRecommendations = listOf(
        Recommendation("Cashback up to", "80%*", "On domestic flights", R.drawable.bg_recommended_card),
        Recommendation("up to", "50% Off", "On domestic flights", R.drawable.bg_recommended_card_pink),
        Recommendation("Save up to", "60%*", "On international flights", R.drawable.bg_recommended_card),
        Recommendation("up to", "45% Off", "On domestic flights", R.drawable.bg_recommended_card_pink),
        Recommendation("Cashback up to", "70%*", "On all flights", R.drawable.bg_recommended_card)
    )
    
    private val busRecommendations = listOf(
        Recommendation("Save up to", "30%*", "On bus bookings", R.drawable.bg_recommended_card),
        Recommendation("up to", "25% Off", "On bus bookings", R.drawable.bg_recommended_card_pink),
        Recommendation("Cashback up to", "35%*", "On premium buses", R.drawable.bg_recommended_card),
        Recommendation("up to", "28% Off", "On bus bookings", R.drawable.bg_recommended_card_pink),
        Recommendation("Save up to", "32%*", "On all buses", R.drawable.bg_recommended_card)
    )
    
    private val hotelRecommendations = listOf(
        Recommendation("Cashback up to", "40%*", "On hotel bookings", R.drawable.bg_recommended_card),
        Recommendation("up to", "35% Off", "On hotel bookings", R.drawable.bg_recommended_card_pink),
        Recommendation("Save up to", "45%*", "On luxury hotels", R.drawable.bg_recommended_card),
        Recommendation("up to", "38% Off", "On hotel bookings", R.drawable.bg_recommended_card_pink),
        Recommendation("Cashback up to", "42%*", "On all hotels", R.drawable.bg_recommended_card)
    )
    
    private val destinations = mutableListOf(
        Destination("Egyupt", "Come to Visit", R.drawable.unlock1, "₹", R.drawable.aeroplane, true, true, false),
        Destination("Egyupt", "Come to Visit", R.drawable.unlock2, "₹", R.drawable.aeroplane, true, true, false),
        Destination("Egyupt", "Come to Visit", R.drawable.unlock1, "₹", R.drawable.aeroplane, true, true, false),
        Destination("Egyupt", "Come to Visit", R.drawable.unlock2, "₹", R.drawable.aeroplane, true, true, false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.ivNotification.setOnClickListener {
            try {
                val activity = requireActivity() as? DashboardActivity
                val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as? androidx.navigation.fragment.NavHostFragment
                navHostFragment?.navController?.let { navController ->
                    activity.navigateToNotification(navController)
                } ?: run {
                    findNavController().navigate(R.id.action_dashboardFragment_to_notificationFragment)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    findNavController().navigate(R.id.action_dashboardFragment_to_notificationFragment)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        
        binding.ivWallet.setOnClickListener {
            try {
                val activity = requireActivity() as? DashboardActivity
                val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as? androidx.navigation.fragment.NavHostFragment
                navHostFragment?.navController?.let { navController ->
                    activity.navigateToWallet(navController)
                } ?: run {
                    findNavController().navigate(R.id.action_dashboardFragment_to_walletFragment)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    findNavController().navigate(R.id.action_dashboardFragment_to_walletFragment)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        
        binding.tvViewAllOffers.setOnClickListener {
            try {
                val activity = requireActivity() as? DashboardActivity
                val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as? androidx.navigation.fragment.NavHostFragment
                navHostFragment?.navController?.let { navController ->
                    activity.navigateToOffers(navController)
                } ?: run {
                    findNavController().navigate(R.id.action_dashboardFragment_to_offersFragment)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    findNavController().navigate(R.id.action_dashboardFragment_to_offersFragment)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        
        setupCategoriesGrid()
        setupExpandCollapseButton()
        setupOffersSlider()
        setupRecommendedSection()
        setupDestinationsSection()
    }
    
    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }
    
    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
        _binding = null
    }

    private fun setupCategoriesGrid() {
        val initialCategories = allCategories.take(4)
        categoriesAdapter = CategoriesAdapter(initialCategories) { category ->
            onCategoryClick(category)
        }
        
        val layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvCategories.layoutManager = layoutManager
        binding.rvCategories.setHasFixedSize(false)
        binding.rvCategories.adapter = categoriesAdapter
        binding.rvCategories.visibility = View.VISIBLE
        binding.rvCategories.isNestedScrollingEnabled = false
        
        binding.rvCategories.post {
            categoriesAdapter.notifyDataSetChanged()
        }
    }

    private fun setupExpandCollapseButton() {
        binding.ivExpandCollapse.setOnClickListener {
            isExpanded = !isExpanded
            
            val fromRotation = if (isExpanded) 0f else 180f
            val toRotation = if (isExpanded) 180f else 0f
            val rotateAnimation = RotateAnimation(
                fromRotation,
                toRotation,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f
            )
            rotateAnimation.duration = 300
            rotateAnimation.fillAfter = true
            binding.ivExpandCollapse.startAnimation(rotateAnimation)
            
            val categoriesToShow = if (isExpanded) allCategories else allCategories.take(4)
            categoriesAdapter.updateCategories(categoriesToShow)
        }
    }

    private fun setupOffersSlider() {
        offersAdapter = OffersAdapter(allOffers)
        
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvOffers.layoutManager = layoutManager
        binding.rvOffers.adapter = offersAdapter
        binding.rvOffers.setHasFixedSize(false)
        
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvOffers)
        
        setupPaginationDots(binding.llPaginationDots, allOffers.size)
        
        binding.rvOffers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                val position = layoutManager?.findFirstVisibleItemPosition() ?: 0
                if (position != currentPosition) {
                    currentPosition = position
                    updatePaginationDots(binding.llPaginationDots, position)
                    stopAutoScroll()
                    startAutoScroll(binding.rvOffers)
                }
            }
        })
        
        startAutoScroll(binding.rvOffers)
    }
    
    private fun setupPaginationDots(container: LinearLayout, count: Int) {
        container.removeAllViews()
        val selectedDotSize = (10 * resources.displayMetrics.density).toInt()
        val unselectedDotSize = (8 * resources.displayMetrics.density).toInt()
        val margin = (6 * resources.displayMetrics.density).toInt()
        
        for (i in 0 until count) {
            val dot = View(requireContext())
            val params = LinearLayout.LayoutParams(unselectedDotSize, unselectedDotSize)
            params.setMargins(margin, 0, margin, 0)
            dot.layoutParams = params
            dot.background = ContextCompat.getDrawable(requireContext(), R.drawable.pagination_dot_unselected)
            container.addView(dot)
        }
        if (container.childCount > 0) {
            val firstDot = container.getChildAt(0)
            val params = firstDot.layoutParams as LinearLayout.LayoutParams
            params.width = selectedDotSize
            params.height = selectedDotSize
            firstDot.layoutParams = params
            firstDot.background = ContextCompat.getDrawable(requireContext(), R.drawable.pagination_dot_selected)
        }
    }
    
    private fun updatePaginationDots(container: LinearLayout, position: Int) {
        val selectedDotSize = (10 * resources.displayMetrics.density).toInt()
        val unselectedDotSize = (8 * resources.displayMetrics.density).toInt()
        
        for (i in 0 until container.childCount) {
            val dot = container.getChildAt(i)
            val params = dot.layoutParams as LinearLayout.LayoutParams
            
            if (i == position) {
                params.width = selectedDotSize
                params.height = selectedDotSize
                dot.layoutParams = params
                dot.background = ContextCompat.getDrawable(requireContext(), R.drawable.pagination_dot_selected)
            } else {
                params.width = unselectedDotSize
                params.height = unselectedDotSize
                dot.layoutParams = params
                dot.background = ContextCompat.getDrawable(requireContext(), R.drawable.pagination_dot_unselected)
            }
        }
    }
    
    private fun startAutoScroll(recyclerView: RecyclerView? = null) {
        stopAutoScroll()
        
        val rvOffers = recyclerView ?: binding.rvOffers
        if (!isAdded) return
        
        autoScrollHandler = Handler(Looper.getMainLooper())
        autoScrollRunnable = object : Runnable {
            override fun run() {
                if (!isAdded) return
                val layoutManager = rvOffers.layoutManager as? LinearLayoutManager
                val currentPos = layoutManager?.findFirstVisibleItemPosition() ?: 0
                val nextPos = if (currentPos < allOffers.size - 1) currentPos + 1 else 0
                
                rvOffers.smoothScrollToPosition(nextPos)
                currentPosition = nextPos
                
                updatePaginationDots(binding.llPaginationDots, nextPos)
                
                autoScrollHandler?.postDelayed(this, 3000)
            }
        }
        autoScrollHandler?.postDelayed(autoScrollRunnable!!, 3000)
    }
    
    private fun stopAutoScroll() {
        autoScrollRunnable?.let { autoScrollHandler?.removeCallbacks(it) }
        autoScrollRunnable = null
    }

    private fun setupRecommendedSection() {
        recommendedAdapter = RecommendedAdapter(flightRecommendations) { recommendation ->
            Toast.makeText(requireContext(), "${recommendation.title} ${recommendation.value} - ${recommendation.description}", Toast.LENGTH_SHORT).show()
        }
        
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommended.layoutManager = layoutManager
        binding.rvRecommended.adapter = recommendedAdapter
        binding.rvRecommended.setHasFixedSize(false)
        
        binding.clTabFlight.setOnClickListener {
            selectTab(0)
            updateRecommendedItems(flightRecommendations)
        }
        
        binding.clTabBus.setOnClickListener {
            selectTab(1)
            updateRecommendedItems(busRecommendations)
        }
        
        binding.clTabHotels.setOnClickListener {
            selectTab(2)
            updateRecommendedItems(hotelRecommendations)
        }
        
        selectTab(0)
    }
    
    private fun selectTab(tabIndex: Int) {
        selectedTab = tabIndex
        
        binding.tvTabFlight.setTextColor(ContextCompat.getColor(requireContext(), R.color.bottom_nav_unselected))
        binding.tvTabBus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bottom_nav_unselected))
        binding.tvTabHotels.setTextColor(ContextCompat.getColor(requireContext(), R.color.bottom_nav_unselected))
        
        binding.ivTabFlightIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bottom_nav_unselected))
        binding.ivTabBusIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bottom_nav_unselected))
        binding.ivTabHotelsIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bottom_nav_unselected))
        
        binding.viewTabFlightDivider.visibility = View.GONE
        binding.viewTabBusDivider.visibility = View.GONE
        binding.viewTabHotelsDivider.visibility = View.GONE
        
        when (tabIndex) {
            0 -> {
                binding.tvTabFlight.setTextColor(ContextCompat.getColor(requireContext(), R.color.bottom_nav_selected))
                binding.ivTabFlightIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bottom_nav_selected))
                binding.viewTabFlightDivider.visibility = View.VISIBLE
            }
            1 -> {
                binding.tvTabBus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bottom_nav_selected))
                binding.ivTabBusIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bottom_nav_selected))
                binding.viewTabBusDivider.visibility = View.VISIBLE
            }
            2 -> {
                binding.tvTabHotels.setTextColor(ContextCompat.getColor(requireContext(), R.color.bottom_nav_selected))
                binding.ivTabHotelsIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bottom_nav_selected))
                binding.viewTabHotelsDivider.visibility = View.VISIBLE
            }
        }
    }
    
    private fun updateRecommendedItems(items: List<Recommendation>) {
        recommendedAdapter = RecommendedAdapter(items) { recommendation ->
            Toast.makeText(requireContext(), "${recommendation.title} ${recommendation.value} - ${recommendation.description}", Toast.LENGTH_SHORT).show()
        }
        binding.rvRecommended.adapter = recommendedAdapter
    }
    
    private fun setupDestinationsSection() {
        destinationsAdapter = DestinationsAdapter(destinations) { destination ->
            Toast.makeText(requireContext(), "Favorite clicked: ${destination.name}", Toast.LENGTH_SHORT).show()
        }
        
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDestinations.layoutManager = layoutManager
        binding.rvDestinations.adapter = destinationsAdapter
        binding.rvDestinations.setHasFixedSize(false)
    }

    private fun onCategoryClick(category: Category) {
        when (category.name) {
            "Flights" -> {
                try {
                    findNavController().navigate(R.id.action_dashboardFragment_to_homeFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
