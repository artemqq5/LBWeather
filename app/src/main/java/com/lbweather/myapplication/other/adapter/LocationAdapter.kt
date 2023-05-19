package com.lbweather.myapplication.other.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbweather.myapplication.R
import com.lbweather.myapplication.other.adapter.LocationAdapter.ViewHolder
import com.lbweather.myapplication.databinding.ItemLocationBinding
import com.lbweather.myapplication.other.location.LocationModel
import com.lbweather.myapplication.other.sharedPreferences.WeatherPref.getShPrefLocation

class LocationAdapter(var dataSet: List<LocationModel>) :
    RecyclerView.Adapter<ViewHolder>() {

    var callBack: ((LocationModel) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemLocationBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bindItem(locationItem: LocationModel, index: Int, size: Int) {

            Log.i("myLog location adapter", "index = $index, size = $size")

            // set active status to current location
            if (locationItem.locality == getShPrefLocation()?.locality) {
                binding.statusUse.visibility = View.VISIBLE
            } else {
                binding.statusUse.visibility = View.INVISIBLE
            }

            // set margin bottom to last element of list
            if (index == size - 1) {
                val param = binding.root.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(10, 10, 10, 10)
                binding.root.layoutParams = param
            }

            // set data to item
            binding.cityCountryInfo.text = locationItem.locality
            binding.latCoordinate.text = "lat ${locationItem.lat}"
            binding.lonCoordinate.text = "lon ${locationItem.lon}"

            binding.root.setOnClickListener {
                callBack?.let { call -> call(locationItem) }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataSet[position], position, itemCount)
    }

    override fun getItemCount(): Int = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun doNotifyDataSetChanged() {
        notifyDataSetChanged()
    }


    fun setList(newList: List<LocationModel>) {
        dataSet = newList
        doNotifyDataSetChanged()
    }

}