package com.example.fliet

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.fliet.databinding.ActivityDashboardBinding


class DashboardActivity : AppCompatActivity() {
    
    private lateinit var binding:  ActivityDashboardBinding
    private var currentSelectedItem: View? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup custom bottom navigation
        setupCustomBottomNavigation(navController)
        
        // Set initial selected item (Home)
        selectBottomNavItem(binding.navItemHome, R.id.dashboardFragment, navController, false)
    }
    
    private fun setupCustomBottomNavigation(navController: androidx.navigation.NavController) {
        // Home item
        binding.navItemHome.setOnClickListener {
            selectBottomNavItem(binding.navItemHome, R.id.dashboardFragment, navController, true)
        }
        
        // Routes item
        binding.navItemRoutes.setOnClickListener {
            selectBottomNavItem(binding.navItemRoutes, R.id.homeFragment, navController, true)
        }
        
        // Discount/Offers item
        binding.navItemDiscount.setOnClickListener {
            navigateToOffers(navController)
        }
        
        // Profile item
        binding.navItemProfile.setOnClickListener {
            navigateToProfile(navController)
        }
    }
    
    private fun selectBottomNavItem(
        itemView: View,
        destinationId: Int,
        navController: androidx.navigation.NavController,
        animate: Boolean
    ) {
        // Deselect previous item
        currentSelectedItem?.let { previousView ->
            deselectItem(previousView, animate)
        }
        
        // Select new item
        selectItem(itemView, animate)
        currentSelectedItem = itemView
        
        // Navigate
        try {
            navController.navigate(destinationId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun selectItem(itemView: View, animate: Boolean) {
        val iconView = when {
            itemView.id == R.id.nav_item_home -> binding.iconHome
            itemView.id == R.id.nav_item_routes -> binding.iconRoutes
            itemView.id == R.id.nav_item_discount -> binding.iconDiscount
            itemView.id == R.id.nav_item_profile -> binding.iconProfile
            else -> null
        }
        
        val labelView = when {
            itemView.id == R.id.nav_item_home -> binding.labelHome
            itemView.id == R.id.nav_item_routes -> binding.labelRoutes
            itemView.id == R.id.nav_item_discount -> binding.labelDiscount
            itemView.id == R.id.nav_item_profile -> binding.labelProfile
            else -> null
        }
        
        if (iconView != null && labelView != null) {
            // Set selected state for background
            itemView.isSelected = true
            
            // Update colors
            iconView.setColorFilter(ContextCompat.getColor(this, R.color.white))
            labelView.setTextColor(ContextCompat.getColor(this, R.color.white))
            
            // Show label with animation
            if (animate) {
                labelView.visibility = View.VISIBLE
                labelView.alpha = 0f
                labelView.translationX = -20f
                
                val fadeIn = ObjectAnimator.ofFloat(labelView, "alpha", 0f, 1f)
                val slideIn = ObjectAnimator.ofFloat(labelView, "translationX", -20f, 0f)
                val scaleUp = ObjectAnimator.ofFloat(itemView, "scaleX", 0.95f, 1f)
                val scaleUpY = ObjectAnimator.ofFloat(itemView, "scaleY", 0.95f, 1f)
                
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(fadeIn, slideIn, scaleUp, scaleUpY)
                animatorSet.duration = 250
                animatorSet.interpolator = DecelerateInterpolator()
                animatorSet.start()
            } else {
                labelView.visibility = View.VISIBLE
                labelView.alpha = 1f
            }
        }
    }
    
    private fun deselectItem(itemView: View, animate: Boolean) {
        val iconView = when {
            itemView.id == R.id.nav_item_home -> binding.iconHome
            itemView.id == R.id.nav_item_routes -> binding.iconRoutes
            itemView.id == R.id.nav_item_discount -> binding.iconDiscount
            itemView.id == R.id.nav_item_profile -> binding.iconProfile
            else -> null
        }
        
        val labelView = when {
            itemView.id == R.id.nav_item_home -> binding.labelHome
            itemView.id == R.id.nav_item_routes -> binding.labelRoutes
            itemView.id == R.id.nav_item_discount -> binding.labelDiscount
            itemView.id == R.id.nav_item_profile -> binding.labelProfile
            else -> null
        }
        
        if (iconView != null && labelView != null) {
            // Remove selected state for background
            itemView.isSelected = false
            
            // Update colors
            iconView.setColorFilter(ContextCompat.getColor(this, R.color.bottom_nav_unselected))
            labelView.setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_unselected))
            
            // Hide label with animation
            if (animate) {
                val fadeOut = ObjectAnimator.ofFloat(labelView, "alpha", 1f, 0f)
                val slideOut = ObjectAnimator.ofFloat(labelView, "translationX", 0f, -20f)
                val scaleDown = ObjectAnimator.ofFloat(itemView, "scaleX", 1f, 0.95f)
                val scaleDownY = ObjectAnimator.ofFloat(itemView, "scaleY", 1f, 0.95f)
                
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(fadeOut, slideOut, scaleDown, scaleDownY)
                animatorSet.duration = 200
                animatorSet.interpolator = DecelerateInterpolator()
                animatorSet.start()
                
                animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        labelView.visibility = View.GONE
                    }
                })
            } else {
                labelView.visibility = View.GONE
                labelView.alpha = 0f
            }
        }
    }
    
    fun navigateToOffers(navController: androidx.navigation.NavController) {
        try {
            val currentDestination = navController.currentDestination?.id
            when (currentDestination) {
                R.id.dashboardFragment -> {
                    navController.navigate(R.id.action_dashboardFragment_to_offersFragment)
                    selectBottomNavItem(binding.navItemDiscount, R.id.offersFragment, navController, true)
                }
                R.id.homeFragment -> {
                    navController.navigate(R.id.action_homeFragment_to_offersFragment)
                    selectBottomNavItem(binding.navItemDiscount, R.id.offersFragment, navController, true)
                }
                R.id.searchResultsFragment -> {
                    navController.navigate(R.id.action_searchResultsFragment_to_offersFragment)
                    selectBottomNavItem(binding.navItemDiscount, R.id.offersFragment, navController, true)
                }
                R.id.offersFragment -> {
                    // Already on offers page, just update selection
                    selectBottomNavItem(binding.navItemDiscount, R.id.offersFragment, navController, false)
                    return
                }
                else -> {
                    // For any other fragment, try direct navigation first
                    try {
                        navController.navigate(R.id.offersFragment)
                        selectBottomNavItem(binding.navItemDiscount, R.id.offersFragment, navController, true)
                    } catch (e: Exception) {
                        // If direct navigation fails, navigate to dashboard first, then to offers
                        navController.navigate(R.id.dashboardFragment)
                        binding.root.postDelayed({
                            try {
                                if (navController.currentDestination?.id == R.id.dashboardFragment) {
                                    navController.navigate(R.id.action_dashboardFragment_to_offersFragment)
                                    selectBottomNavItem(binding.navItemDiscount, R.id.offersFragment, navController, true)
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }, 150)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: try direct navigation
            try {
                navController.navigate(R.id.offersFragment)
                selectBottomNavItem(binding.navItemDiscount, R.id.offersFragment, navController, true)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
    
    private fun navigateToProfile(navController: androidx.navigation.NavController) {
        try {
            val currentDestination = navController.currentDestination?.id
            if (currentDestination == R.id.profileFragment) {
                // Already on profile page, just update selection
                selectBottomNavItem(binding.navItemProfile, R.id.profileFragment, navController, false)
                return
            }
            
            when (currentDestination) {
                R.id.dashboardFragment -> {
                    navController.navigate(R.id.action_dashboardFragment_to_profileFragment)
                    selectBottomNavItem(binding.navItemProfile, R.id.profileFragment, navController, true)
                }
                R.id.homeFragment -> {
                    navController.navigate(R.id.action_homeFragment_to_profileFragment)
                    selectBottomNavItem(binding.navItemProfile, R.id.profileFragment, navController, true)
                }
                R.id.searchResultsFragment -> {
                    navController.navigate(R.id.action_searchResultsFragment_to_profileFragment)
                    selectBottomNavItem(binding.navItemProfile, R.id.profileFragment, navController, true)
                }
                else -> {
                    // For any other fragment, try direct navigation first
                    try {
                        navController.navigate(R.id.profileFragment)
                        selectBottomNavItem(binding.navItemProfile, R.id.profileFragment, navController, true)
                    } catch (e: Exception) {
                        // If direct navigation fails, navigate to dashboard first, then to profile
                        navController.navigate(R.id.dashboardFragment)
                        binding.root.postDelayed({
                            try {
                                if (navController.currentDestination?.id == R.id.dashboardFragment) {
                                    navController.navigate(R.id.action_dashboardFragment_to_profileFragment)
                                    selectBottomNavItem(binding.navItemProfile, R.id.profileFragment, navController, true)
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }, 150)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Final fallback: try direct navigation
            try {
                navController.navigate(R.id.profileFragment)
                selectBottomNavItem(binding.navItemProfile, R.id.profileFragment, navController, true)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
    
    fun navigateToWallet(navController: androidx.navigation.NavController) {
        try {
            val currentDestination = navController.currentDestination?.id
            if (currentDestination == R.id.walletFragment) {
                // Already on wallet page, do nothing
                return
            }
            
            when (currentDestination) {
                R.id.dashboardFragment -> {
                    navController.navigate(R.id.action_dashboardFragment_to_walletFragment)
                }
                R.id.homeFragment -> {
                    // Try using action first
                    try {
                        navController.navigate(R.id.action_homeFragment_to_walletFragment)
                    } catch (e: Exception) {
                        // If action fails, try direct navigation
                        try {
                            navController.navigate(R.id.walletFragment)
                        } catch (e2: Exception) {
                            // If direct navigation fails, navigate to dashboard first, then to wallet
                            navController.navigate(R.id.dashboardFragment)
                            binding.root.postDelayed({
                                try {
                                    if (navController.currentDestination?.id == R.id.dashboardFragment) {
                                        navController.navigate(R.id.action_dashboardFragment_to_walletFragment)
                                    }
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }, 150)
                        }
                    }
                }
                R.id.searchResultsFragment -> {
                    navController.navigate(R.id.action_searchResultsFragment_to_walletFragment)
                }
                else -> {
                    // For any other fragment, try direct navigation first
                    try {
                        navController.navigate(R.id.walletFragment)
                    } catch (e: Exception) {
                        // If direct navigation fails, navigate to dashboard first, then to wallet
                        navController.navigate(R.id.dashboardFragment)
                        binding.root.postDelayed({
                            try {
                                if (navController.currentDestination?.id == R.id.dashboardFragment) {
                                    navController.navigate(R.id.action_dashboardFragment_to_walletFragment)
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }, 150)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Final fallback: try direct navigation
            try {
                navController.navigate(R.id.walletFragment)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
    
    fun navigateToNotification(navController: androidx.navigation.NavController) {
        try {
            val currentDestination = navController.currentDestination?.id
            if (currentDestination == R.id.notificationFragment) {
                // Already on notification page, do nothing
                return
            }
            
            when (currentDestination) {
                R.id.dashboardFragment -> {
                    navController.navigate(R.id.action_dashboardFragment_to_notificationFragment)
                }
                R.id.homeFragment -> {
                    // Try using action first
                    try {
                        navController.navigate(R.id.action_homeFragment_to_notificationFragment)
                    } catch (e: Exception) {
                        // If action fails, try direct navigation
                        try {
                            navController.navigate(R.id.notificationFragment)
                        } catch (e2: Exception) {
                            // If direct navigation fails, navigate to dashboard first, then to notification
                            navController.navigate(R.id.dashboardFragment)
                            binding.root.postDelayed({
                                try {
                                    if (navController.currentDestination?.id == R.id.dashboardFragment) {
                                        navController.navigate(R.id.action_dashboardFragment_to_notificationFragment)
                                    }
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }, 150)
                        }
                    }
                }
                R.id.searchResultsFragment -> {
                    navController.navigate(R.id.action_searchResultsFragment_to_notificationFragment)
                }
                else -> {
                    // For any other fragment, try direct navigation first
                    try {
                        navController.navigate(R.id.notificationFragment)
                    } catch (e: Exception) {
                        // If direct navigation fails, navigate to dashboard first, then to notification
                        try {
                            navController.navigate(R.id.dashboardFragment)
                            binding.root.postDelayed({
                                try {
                                    if (navController.currentDestination?.id == R.id.dashboardFragment) {
                                        navController.navigate(R.id.action_dashboardFragment_to_notificationFragment)
                                    }
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }, 150)
                        } catch (e2: Exception) {
                            e2.printStackTrace()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Final fallback: try direct navigation
            try {
                navController.navigate(R.id.notificationFragment)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
    
    fun hideBottomNavigation() {
        binding.customBottomNav.visibility = View.GONE
    }
    
    fun showBottomNavigation() {
        binding.customBottomNav.visibility = View.VISIBLE
    }
}