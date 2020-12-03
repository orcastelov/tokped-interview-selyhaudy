package com.tokopedia.maps.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.maps.MapsService
import com.tokopedia.maps.model.Country
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MapsViewModel (private val mapsService: MapsService): ViewModel() {

    val allCountries = MutableLiveData<List<Country>>()

    private val compositeDisposable = CompositeDisposable()

    init {
        fetchCountryNames()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun fetchCountryNames() {
        compositeDisposable.add(
                mapsService.getAllCountries()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
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

    fun findCountryByName(countryName: String): Country? {
        val result = allCountries.value?.find {
            it.name.equals(countryName, true)
        }
        return result
    }
}