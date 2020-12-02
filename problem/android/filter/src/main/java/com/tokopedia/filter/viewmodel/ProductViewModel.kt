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


class ProductViewModel(
        private val repository: ProductRepository
) : ViewModel() { // todo: protocol

    private val allProducts = mutableListOf<Product>()
    val activeProducts = MutableLiveData<List<Product>>()
    val activeProductFilter = MutableLiveData<ProductFilter>()
    private var hasInit = false

    fun initData(productLoader: ProductLoader) {
        if (!hasInit) {
            loadAllProducts(productLoader)
            val productFilter = createProductFilter()
            filterAllProducts(productFilter)
            activeProductFilter.postValue(productFilter)
            hasInit = true
        }
    }

    fun applyFilter(
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

    // buat repo + product DB / File Loader
    private fun loadAllProducts(productLoader: ProductLoader) {
        allProducts.addAll(repository.loadProduct(productLoader, R.raw.products)) // todo execute in background
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

    private fun createDefaultProductFilter() = ProductFilter(
            locationFilterList = mutableListOf(LocationFilterItem.Other()),
            priceFilter = PriceFilter(DEFAULT_MINIMUM_PRICE, DEFAULT_MAXIMUM_PRICE)
    )

    companion object {
        const val DEFAULT_MINIMUM_PRICE = 0
        const val DEFAULT_MAXIMUM_PRICE = 1_000_000_000
    }
}




