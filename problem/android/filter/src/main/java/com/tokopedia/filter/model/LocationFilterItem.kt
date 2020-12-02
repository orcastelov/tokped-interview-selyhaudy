package com.tokopedia.filter.model

sealed class LocationFilterItem(
        val title: String,
        var checked: Boolean = false
) {
    class City(val city: String) : LocationFilterItem(city)
    class Other: LocationFilterItem("Other")
}