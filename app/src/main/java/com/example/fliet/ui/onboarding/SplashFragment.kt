package com.example.fliet.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.fliet.R
import com.example.fliet.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        try {
            binding.lottieAnimation.setAnimation(R.raw.flight)
            binding.lottieAnimation.repeatCount = -1
            binding.lottieAnimation.playAnimation()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed({
            navigateToDashboard()
        }, 2500)
    }

    private fun navigateToDashboard() {
        if (!isAdded || view == null) return
        
        try {
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.splashFragment) {
                navController.navigate(R.id.action_splashFragment_to_dashboardFragment)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler?.removeCallbacksAndMessages(null)
        handler = null
        _binding = null
    }
}
