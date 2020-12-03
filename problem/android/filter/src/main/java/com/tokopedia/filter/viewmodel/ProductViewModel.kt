package com.tokopedia.filter.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.filter.model.Product
import com.tokopedia.filter.model.ProductFilter


interface ProductViewModel{

    val activeProducts: LiveData<List<Product>>
    val activeProductFilter: LiveData<ProductFilter>

    fun applyFilter(
            selectedLocation: String?,
            selectedMinimumPrice: Int,
            selectedMaximumPrice: Int
    )

    companion object {
        const val DEFAULT_MINIMUM_PRICE = 0
        const val DEFAULT_MAXIMUM_PRICE = 1_000_000_000
    }
}




