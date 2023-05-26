package com.lbweather.getweatherfromall.presentation.locationsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.databinding.DialogLocationsBinding
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelLocation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogListLocations : DialogFragment(), NavigationInterfaceAdapter {

    private lateinit var binding: DialogLocationsBinding

    private val viewModelLocation: ViewModelLocation by viewModel(ownerProducer = { requireActivity() })

    private val locationAdapter by lazy {
        LocationAdapter(arrayListOf(), this)
    }

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. DialogListLocations ($throwable)")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogLocationsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerLocation.adapter = locationAdapter

        lifecycleScope.launch(excHandler) {
            viewModelLocation.flowLocationList.collectLatest {
                val list = it.map { itemList ->
                    itemList.apply {
                        statusActive =
                            if (this.name == viewModelLocation.getLastCurrentLocation().name) {
                                View.VISIBLE
                            } else {
                                View.INVISIBLE
                            }
                    }.also {
                        logData("${itemList.name} - ${itemList.statusActive}")
                    }
                }

                locationAdapter.setList(list.reversed())
            }
        }

        lifecycleScope.launch(excHandler) {
            viewModelLocation.flowCurrentLocation.collectLatest {
                logData("current location is (${it?.name})")
                it?.let {
                    val list: List<LocationTable> = locationAdapter.dataSet.map { itemList ->
                        itemList.apply {
                            statusActive = if (this.name == it.name) {
                                View.VISIBLE
                            } else {
                                View.INVISIBLE
                            }
                        }.also {
                            logData("${itemList.name} - ${itemList.statusActive}")
                        }
                    }

                    locationAdapter.setList(list)
                }


            }
        }

        // add swipe listener to delete item
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (locationAdapter.dataSet.size > 1) {
                    lifecycleScope.launch(excHandler) {
                        viewModelLocation.deleteLocationData(locationAdapter.dataSet[viewHolder.adapterPosition])
                        if (viewHolder.adapterPosition == 0) {
                            viewModelLocation.setCurrentLocationData(locationAdapter.dataSet[1])
                        } else viewModelLocation.setCurrentLocationData(locationAdapter.dataSet[0])

                    }
                }
            }

        }).attachToRecyclerView(binding.recyclerLocation)


        binding.currentLocation.setOnClickListener {
            try {
                viewModelLocation.checkLocationPermission()
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error to get location", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun changeCurrentLocation(location: LocationTable) {
        viewModelLocation.setCurrentLocationData(location)
    }
}