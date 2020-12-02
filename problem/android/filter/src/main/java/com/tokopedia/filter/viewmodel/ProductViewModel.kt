package com.tokopedia.filter.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.filter.R
import com.tokopedia.filter.model.*
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


class ProductViewModel: ViewModel() {

    private val allProducts = mutableListOf<Product>()
    val activeProducts = MutableLiveData<List<Product>>()
    val activeProductFilter = MutableLiveData<ProductFilter>()
    private var hasInit = false

    fun initData(context: Context){
        if(!hasInit){
            loadAllProducts(context)
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

    private fun loadAllProducts(context: Context) {
        val productJsonText = readProductJsonFile(context) ?: return
        val productJson = JSONObject(productJsonText)
        val productArray = productJson.getJSONObject("data").getJSONArray("products")

        allProducts.clear()

        for (i in 0 until productArray.length()) {
            val product = productArray.getJSONObject(i)
            val shop = product.getJSONObject("shop")
            allProducts.add(
                    Product(
                            id = product.getInt("id"),
                            name = product.getString("name"),
                            imageUrl = product.getString("imageUrl"),
                            priceInt = product.getInt("priceInt"),
                            discountPercentage = product.getInt("discountPercentage"),
                            slashedPriceInt = product.getInt("slashedPriceInt"),
                            shop = Shop(
                                    id = shop.getInt("id"),
                                    name = shop.getString("name"),
                                    city = shop.getString("city")
                            )
                    )
            )
        }
    }

    private fun readProductJsonFile(context: Context): String? {
        var json: String? = null
        json = try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.products)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun filterAllProducts(productFilter: ProductFilter?) {
        if(productFilter == null){
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

    private fun createProductFilter(): ProductFilter{
        // City Filter
        val cityFrequency = allProducts.groupingBy {
            it.shop.city
        }.eachCount()
        val sortedCity = cityFrequency.keys.sortedByDescending {
            cityFrequency[it]
        }
        val filterItem = mutableListOf<LocationFilterItem>()
        for(i in sortedCity.indices){
            if( i > 1 ){// only 2 items
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




