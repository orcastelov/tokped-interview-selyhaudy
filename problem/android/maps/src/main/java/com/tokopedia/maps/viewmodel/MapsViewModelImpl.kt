package com.tokopedia.maps.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.maps.scheduler.BaseSchedulerProvider
import com.tokopedia.maps.MapsService
import com.tokopedia.maps.model.Country
import io.reactivex.disposables.CompositeDisposable

class MapsViewModelImpl (
        private val schedulerProvider: BaseSchedulerProvider,
        private val mapsService: MapsService
): MapsViewModel, ViewModel() {

    override val allCountries = MutableLiveData<List<Country>>()

    private val compositeDisposable = CompositeDisposable()

    init {
        fetchCountryNames()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    override fun findCountryByName(countryName: String): Country? {
        val result = allCountries.value?.find {
            it.name.equals(countryName, true)
        }
        return result
    }

    private fun fetchCountryNames() {
        compositeDisposable.add(
                mapsService.getAllCountries()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .map {
                            it.map {
                                it.toModel()
                            }
                        }
                        .subscribe(::handleAllCountries)
        )
    }

    private fun handleAllCountries(countries: List<Country>){
        allCountries.value = countries
    }
}