package com.tokopedia.filter.model

data class Product(
        val id: Int,
        val name: String,
        val imageUrl: String,
        val priceInt: Int,
        val discountPercentage: Int,
        val slashedPriceInt: Int,
        val shop: Shop

)