package com.lbweather.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.lbweather.myapplication.domain.network.ConnectionManager
import com.lbweather.myapplication.domain.repository.DefaultRepository
import kotlinx.coroutines.flow.Flow

class ViewModelInternet(
    private val repository: DefaultRepository
): ViewModel() {

    val internetFlowData: Flow<ConnectionManager.StatusInternet>
        get() = repository.getConnectionFlow()

    fun getStatusNow(): Boolean {
        return repository.isInternetActive()
    }

}