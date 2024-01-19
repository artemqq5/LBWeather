package com.lbweather.getweatherfromall.domain.usecase

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R

object GlideLoaderUseCase {

    // load image from url with Glide
    fun ImageView.loadImg(url: String) {
        try {
            Glide
                .with(this.context)
                .load("https:$url")
                .placeholder(R.drawable.ic_image)
                .centerCrop()
                .into(this)
        } catch (e: Exception) {
            logData(e)
        }

    }

}