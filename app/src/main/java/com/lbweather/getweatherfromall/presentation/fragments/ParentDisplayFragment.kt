package com.lbweather.getweatherfromall.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.FragmentParentDisplayBinding
import com.lbweather.getweatherfromall.presentation.adapters.CustomPagerAdapter

class ParentDisplayFragment : Fragment() {

//    private var mInterstitialAd: InterstitialAd? = null

    private var _binding: FragmentParentDisplayBinding? = null
    private val binding: FragmentParentDisplayBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentParentDisplayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Disable swipe
        binding.viewPager.isUserInputEnabled = false

        // Set the adapter
        binding.viewPager.adapter = CustomPagerAdapter(this)

        // Connect the TabLayout and the ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = resources.getString(R.string.current_weather)
                }

                1 -> {
                    tab.text = resources.getString(R.string.for_3_days)
                }
            }
        }.attach()

//        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//            override fun onAdClicked() {
//                // Called when a click is recorded for an ad.
////                log("Ad was clicked.")
//            }
//
//            override fun onAdDismissedFullScreenContent() {
//                // Called when ad is dismissed.
////                log("Ad dismissed fullscreen content.")
//                mInterstitialAd = null
//            }
//
//            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                // Called when ad fails to show.
////                log("Ad failed to show fullscreen content.")
//                mInterstitialAd = null
//            }
//
//            override fun onAdImpression() {
//                // Called when an impression is recorded for an ad.
////                log("Ad recorded an impression.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                // Called when ad is shown.
////                log("Ad showed fullscreen content.")
//            }
//        }

    }

//    private fun loadAds() {
//        val adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(
//            requireContext(),
//            UseCaseGoogleAds.ID_INTERSTITIAL,
//            adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
////                log(adError.toString())
//                    mInterstitialAd = null
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
////                log("Ad was loaded.")
//                    mInterstitialAd = interstitialAd
//                    mInterstitialAd?.show(requireActivity())
//                }
//            })
//    }
}

