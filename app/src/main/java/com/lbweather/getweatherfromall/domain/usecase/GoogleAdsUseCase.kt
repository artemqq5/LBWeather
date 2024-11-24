package com.lbweather.getweatherfromall.domain.usecase

import com.lbweather.getweatherfromall.BuildConfig

class GoogleAdsUseCase {
    companion object {
        var stateShowed = false
        const val ID_ON_START_ACTIVITY_INTERSTITIAL = BuildConfig.ID_ON_START_ACTIVITY_INTERSTITIAL
    }
}