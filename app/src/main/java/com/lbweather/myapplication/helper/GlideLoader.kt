package com.lbweather.myapplication.helper

import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.lbweather.myapplication.R
import com.lbweather.myapplication.MainActivity.Companion.main_context

object GlideLoader {

    // load image from url with Glide
    fun ImageView.loadImg(url: String) {
        try {
            Glide
                .with(main_context)
                .load("https:$url")
                .placeholder(R.drawable.ic_image)
                .centerCrop()
                .into(this)
        } catch (e: Exception) {
            Toast.makeText(main_context, e.message, Toast.LENGTH_SHORT).show()
        }

    }

}