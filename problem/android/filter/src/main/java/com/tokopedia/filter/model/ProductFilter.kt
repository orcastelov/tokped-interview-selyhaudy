package com.tokopedia.filter.model

data class ProductFilter(
        val locationFilterList: MutableList<LocationFilterItem>,
        val priceFilter: PriceFilter
)
