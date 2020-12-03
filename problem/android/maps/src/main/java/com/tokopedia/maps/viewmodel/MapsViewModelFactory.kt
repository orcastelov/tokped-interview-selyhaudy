package com.tokopedia.maps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.maps.scheduler.BaseSchedulerProvider
import com.tokopedia.maps.scheduler.SchedulerProvider
import com.tokopedia.maps.MapsService
import com.tokopedia.maps.MapsService.Companion.mapsService

class MapsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
                .getConstructor(BaseSchedulerProvider::class.java, MapsService::class.java)
                .newInstance(SchedulerProvider(), mapsService)
    }
}