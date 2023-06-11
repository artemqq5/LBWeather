package com.lbweather.getweatherfromall.presentation.displayFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.ItemRecyclerViewBinding
import com.lbweather.getweatherfromall.domain.UseCaseSettings
import com.lbweather.getweatherfromall.domain.model.weather.Hour
import com.lbweather.getweatherfromall.helper.GlideLoader.loadImg

class CustomAdapter(
    private var dataList: ArrayList<Hour>,
    private val useCaseSettings: UseCaseSettings,
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRecyclerViewBinding.bind(view)

        fun initAll(dataModel: Hour) {
            binding.imageWeather.loadImg(dataModel.condition.icon)

            // set time in format that you choice (24h or 12h)
            binding.time.text = dataModel.timeAA(useCaseSettings.getTimeFormat())

            // set temp in unit that you choice (celsius or fahrenheit)
            binding.dataInfo.text =
                if (useCaseSettings.getTempUnit() == UseCaseSettings.TempUnit.CELSIUS) dataModel.tempCParsed
                else dataModel.tempFParsed
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initAll(dataList[position])
    }

    override fun getItemCount() = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: ArrayList<Hour>) {
        dataList = newList
        notifyDataSetChanged()
    }

}