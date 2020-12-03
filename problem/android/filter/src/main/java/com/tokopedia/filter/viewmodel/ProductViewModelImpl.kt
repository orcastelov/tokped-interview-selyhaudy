package com.tokopedia.filter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.filter.R
import com.tokopedia.filter.datasource.ProductLoader
import com.tokopedia.filter.model.LocationFilterItem
import com.tokopedia.filter.model.PriceFilter
import com.tokopedia.filter.model.Product
import com.tokopedia.filter.model.ProductFilter
import com.tokopedia.filter.repo.ProductRepository
import com.tokopedia.filter.scheduler.BaseSchedulerProvider
import com.tokopedia.filter.viewmodel.ProductViewModel.Companion.DEFAULT_MAXIMUM_PRICE
import com.tokopedia.filter.viewmodel.ProductViewModel.Companion.DEFAULT_MINIMUM_PRICE
import io.reactivex.disposables.CompositeDisposable


class ProductViewModelImpl(
        private val schedulerProvider: BaseSchedulerProvider,
        private val productLoader: ProductLoader,
        private val repository: ProductRepository
) : ProductViewModel, ViewModel() {

    override val activeProducts = MutableLiveData<List<Product>>()
    override val activeProductFilter = MutableLiveData<ProductFilter>()

    private val allProducts = mutableListOf<Product>()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadAllProducts()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    override fun applyFilter(
            selectedLocation: String?,
            selectedMinimumPrice: Int,
            selectedMaximumPrice: Int
    ) {
        val lastProductFilter = activeProductFilter.value ?: return

        // update selected location
        lastProductFilter.locationFilterList.apply {
            forEach {
                it.checked = false
            }
            find {
                it.title == selectedLocation
            }?.checked = true
        }

        // update selected price range
        lastProductFilter.priceFilter.apply {
            this.selectedMinimumPrice = selectedMinimumPrice
            this.selectedMaximumPrice = selectedMaximumPrice
        }

        filterAllProducts(lastProductFilter)
        activeProductFilter.postValue(lastProductFilter)
    }

    private fun loadAllProducts() {
        compositeDisposable.add(
                repository.loadProduct(productLoader, R.raw.products)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe {
                            allProducts.addAll(it)
                            val productFilter = createProductFilter()
                            filterAllProducts(productFilter)
                            activeProductFilter.postValue(productFilter)
                        }
        )
    }

    private fun filterAllProducts(productFilter: ProductFilter?) {
        if (productFilter == null) {
            activeProducts.postValue(allProducts)
            return
        }

        val selectedLocation = productFilter.locationFilterList.firstOrNull { it.checked }
        val topLocations = productFilter.locationFilterList.filter {
            it is LocationFilterItem.City
        }.map {
            it.title
        }

        activeProducts.postValue(
                allProducts.filter { product ->
                    val inPriceRange = product.priceInt >= productFilter.priceFilter.selectedMinimumPrice
                            && product.priceInt <= productFilter.priceFilter.selectedMaximumPrice
                    val validCity = when (selectedLocation) {
                        null -> true // no selected filter
                        is LocationFilterItem.City -> selectedLocation.city == product.shop.city
                        is LocationFilterItem.Other -> !topLocations.contains(product.shop.city)
                    }

                    inPriceRange && validCity
                }
        )
    }

    private fun createProductFilter(): ProductFilter {
        // City Filter
        val cityFrequency = allProducts.groupingBy {
            it.shop.city
        }.eachCount()
        val sortedCity = cityFrequency.keys.sortedByDescending {
            cityFrequency[it]
        }
        val filterItem = mutableListOf<LocationFilterItem>()
        for (i in sortedCity.indices) {
            if (i > 1) {// only 2 items
                break
            }
            filterItem.add(
                    LocationFilterItem.City(sortedCity[i])
            )
        }
        filterItem.add(LocationFilterItem.Other())

        // Price Filter
        val minimumPrice = allProducts.minBy { it.priceInt }?.priceInt ?: DEFAULT_MINIMUM_PRICE
        val maximumPrice = allProducts.maxBy { it.priceInt }?.priceInt ?: DEFAULT_MAXIMUM_PRICE

        return ProductFilter(
                locationFilterList = filterItem,
                priceFilter = PriceFilter(minimumPrice, maximumPrice)
        )
    }
}




