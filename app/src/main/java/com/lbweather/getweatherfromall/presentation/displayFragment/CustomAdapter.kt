package com.lbweather.getweatherfromall.presentation.displayFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.ItemRecyclerViewBinding
import com.lbweather.getweatherfromall.other.helper.GlideLoader.loadImg
import com.lbweather.getweatherfromall.domain.model.weather.Hour

class CustomAdapter(private var dataList: ArrayList<Hour>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRecyclerViewBinding.bind(view)

        fun initAll(dataModel: Hour) {

            binding.time.text = dataModel.timeAA

            binding.imageWeather.loadImg(dataModel.condition.icon)
            binding.dataInfo.text = dataModel.tempCParsed
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