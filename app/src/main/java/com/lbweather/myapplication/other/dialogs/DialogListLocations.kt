package com.lbweather.myapplication.other.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lbweather.myapplication.R
import com.lbweather.myapplication.other.adapter.LocationAdapter
import com.lbweather.myapplication.databinding.DialogLocationsBinding
import com.lbweather.myapplication.other.location.LocationModel
import com.lbweather.myapplication.other.viewmodel.ViewModelLocation

class DialogListLocations : DialogFragment() {

    private lateinit var binding: DialogLocationsBinding

    private val viewModelLocation: ViewModelLocation by activityViewModels()

    private val locationAdapter by lazy {
        LocationAdapter(arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogLocationsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationAdapter.callBack = ::updateData
        binding.recyclerLocation.adapter = locationAdapter


        viewModelLocation.listOfLocation.observe(viewLifecycleOwner) {
            locationAdapter.setList(it.reversed())
        }

        viewModelLocation.currentLocation.observe(viewLifecycleOwner) {
            locationAdapter.doNotifyDataSetChanged()
        }

        binding.currentLocation.setOnClickListener {
            viewModelLocation.getLastLocation()
        }

        binding.addNewLocation.setOnClickListener {
            viewModelLocation.updateListOfLocations(
                LocationModel(
                    "51.509865",
                    "-0.118092",
                    "London"
                )
            )
        }


        // add swipe listener to delete item
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                Log.i("myLog, ItemTouchHelper", "${viewHolder.layoutPosition}")
                viewModelLocation.removeLocation(
                    locationAdapter.dataSet[viewHolder.adapterPosition]
                )

            }

        }).attachToRecyclerView(binding.recyclerLocation)


    }

    private fun updateData(location: LocationModel) {
        viewModelLocation.currentLocation.value = location
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
}