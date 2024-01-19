package com.lbweather.getweatherfromall.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.ItemLocationBinding
import com.lbweather.getweatherfromall.domain.model.LocationUserModel
import com.lbweather.getweatherfromall.presentation.adapters.LocationAdapter.ViewHolder

class LocationAdapter(
    var locationList: List<LocationUserModel> = emptyList(),
    private val listenerClick: NavigationCustomAdapter,
) :
    RecyclerView.Adapter<ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemLocationBinding.bind(view)

        fun bindItem(locationItem: LocationUserModel) {

            // set bottom margin to last item of recyclerView
            if (adapterPosition + 1 == locationList.size) {
                val layoutParams = itemView.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = 20
            }

            // set data to item
            binding.cityCountryInfo.text = locationItem.shortLocation
            binding.statusUse.visibility = if (locationItem.status) View.VISIBLE else View.INVISIBLE

            itemView.setOnClickListener {
                binding.statusUse.visibility = View.VISIBLE
                listenerClick.changeCurrentLocation(locationItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(locationList[position])
    }

    override fun getItemCount(): Int = locationList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<LocationUserModel>) {
        locationList = newList.reversed()
        notifyDataSetChanged()
    }

}