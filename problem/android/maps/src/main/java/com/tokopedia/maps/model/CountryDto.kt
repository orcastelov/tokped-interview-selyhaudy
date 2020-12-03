package com.tokopedia.maps.model

data class CountryDto(
        val name: String,
        val capital: String,
        val population: Int,
        val callingCodes: List<String>,
        val latlng: List<Double>
){
    fun toModel(): Country {
        return Country(
                name,
                capital,
                population,
                callingCodes,
                latlng
        )
    }
}