package com.tokopedia.maps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.maps.MapsService
import com.tokopedia.maps.MapsService.Companion.mapsService

class MapsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MapsService::class.java).newInstance(mapsService)
    }
}