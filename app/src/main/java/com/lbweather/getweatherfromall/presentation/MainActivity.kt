package com.lbweather.getweatherfromall.presentation

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase.Companion.ID_ON_START_ACTIVITY_INTERSTITIAL

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var mInterstitialAd: InterstitialAd? = null

    override fun onStart() {
        super.onStart()
        showAds()
    }

    private fun showAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this@MainActivity)

            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    logData("INTERSTITIAL AD was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    logData("INTERSTITIAL AD dismissed fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    logData("INTERSTITIAL AD failed to show fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    logData("INTERSTITIAL AD recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    logData("INTERSTITIAL AD showed fullscreen content.")
                }
            }
        } else {
            loadAds()
        }
    }

    private fun loadAds() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            ID_ON_START_ACTIVITY_INTERSTITIAL,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    logData("INTERSTITIAL AD $adError")
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    logData("INTERSTITIAL AD was loaded.")
                    mInterstitialAd = interstitialAd
                    showAds()
                }
            })
    }


}