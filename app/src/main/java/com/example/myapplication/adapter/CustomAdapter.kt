package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemRecyclerViewBinding
import com.example.myapplication.helper.FromStr.fromStr
import com.example.myapplication.helper.GlideLoader.loadImg
import com.example.myapplication.helper.StateUnit.isCelsius
import com.example.myapplication.helper.StateUnit.isTime24
import com.example.myapplication.helper.TimeFormat.HOUR_MINUTE
import com.example.myapplication.helper.TimeFormat.HOUR_MINUTE_AA
import com.example.myapplication.helper.TimeFormat.YEAR_MONTH_DAY_HOUR_MINUTE
import com.example.myapplication.helper.TimeFormat.getParsingTime
import com.example.myapplication.helper.TimeFormat.getParsingTimeHour
import com.example.myapplication.weatherModelData.Hour

class CustomAdapter(
    private var dataList: ArrayList<Hour>,
    private val controller: NavController
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRecyclerViewBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun initAll(dataModel: Hour, controller2: NavController) {

            // click listener to click on item of recyclerView
            itemView.setOnClickListener {
                // get current object put in arguments and navigate to fragmentHour
                val bundleHour = bundleOf("timeHour" to dataModel)
                controller2.navigate(R.id.action_displayWeather_to_detailsHour, bundleHour)
            }

            // if dataMode == current, do with no date (`Now Date`)
            if (dataModel.chance_of_snow == null || dataModel.chance_of_rain == null) {
                binding.time.text = fromStr(R.string.timeNow)
            } else {
                binding.time.text = if (isTime24()) {
                    dataModel.time.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)
                } else dataModel.time.getParsingTimeHour(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE_AA)
            }

            binding.textWeather.text = dataModel.condition.text
            binding.imageWeather.loadImg(dataModel.condition.icon)
            binding.dataInfo.text = if (isCelsius()) {
                "${dataModel.temp_c} ${fromStr(R.string.celsius)}"
            } else {
                "${dataModel.temp_f} ${fromStr(R.string.fahrenheit)}"
            }

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initAll(dataList[position], controller)
    }

    override fun getItemCount() = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: ArrayList<Hour>) {
        dataList = newList
        notifyDataSetChanged()
    }

}