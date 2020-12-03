package com.tokopedia.maps.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.maps.model.Country

interface MapsViewModel {
    val allCountries: LiveData<List<Country>>
    fun findCountryByName(countryName: String): Country?
}