package com.example.myapplication.viewmodel

import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ViewModelLocation : ViewModel() {


    val currentLocation by lazy {
        MutableLiveData<String>()
    }


}