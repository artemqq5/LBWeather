package com.lbweather.getweatherfromall.presentation

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase.Companion.ID_ON_START_ACTIVITY_INTERSTITIAL
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase.Companion.stateShowed

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        loadAds()
    }

    private fun loadAds() {

        if (stateShowed)
            return

        val adRequest = AdRequest.Builder().build()

        findViewById<FrameLayout>(R.id.loadAdsContainer).visibility = View.VISIBLE

        InterstitialAd.load(
            this,
            ID_ON_START_ACTIVITY_INTERSTITIAL,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    logData("INTERSTITIAL AD $adError")
                    mInterstitialAd = null
                    findViewById<FrameLayout>(R.id.loadAdsContainer).visibility = View.INVISIBLE
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    logData("INTERSTITIAL AD was loaded.")
                    mInterstitialAd = interstitialAd
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
                            findViewById<FrameLayout>(R.id.loadAdsContainer).visibility = View.INVISIBLE
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            logData("INTERSTITIAL AD recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            logData("INTERSTITIAL AD showed fullscreen content.")
                            findViewById<FrameLayout>(R.id.loadAdsContainer).visibility = View.INVISIBLE
                        }
                    }
                    mInterstitialAd?.show(this@MainActivity)
                    stateShowed = true
                }
            })
    }


}