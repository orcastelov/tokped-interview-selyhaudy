package com.tokopedia.maps.model

data class Country(
        val name: String,
        val capital: String,
        val population: Int,
        val callingCodes: List<String>,
        val latlng: List<Double>
)