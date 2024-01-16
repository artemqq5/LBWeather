package com.lbweather.getweatherfromall.domain.optional.helper

import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.lbweather.getweatherfromall.R

object GlideLoader {

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
            Toast.makeText(this.context, e.message, Toast.LENGTH_SHORT).show()
        }

    }

}