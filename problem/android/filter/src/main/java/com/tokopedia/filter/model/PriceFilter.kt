package com.tokopedia.filter.model

class PriceFilter(
        val minimumPrice: Int,
        val maximumPrice: Int
){
    var selectedMinimumPrice: Int = minimumPrice
    var selectedMaximumPrice: Int = maximumPrice
}