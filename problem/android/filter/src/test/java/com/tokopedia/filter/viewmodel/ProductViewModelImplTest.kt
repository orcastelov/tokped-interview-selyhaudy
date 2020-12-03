package com.tokopedia.filter.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.filter.datasource.ProductLoader
import com.tokopedia.filter.model.Product
import com.tokopedia.filter.model.Shop
import com.tokopedia.filter.repo.ProductRepository
import com.tokopedia.filter.scheduler.TrampolineSchedulerProvider
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductViewModelImplTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var productViewModelImpl: ProductViewModelImpl

    @Before
    fun setUp() {
        productViewModelImpl = createViewModel()
    }

    @Test
    fun activeFilter_cityFilterOrder() {
        val locationFilterList = productViewModelImpl.activeProductFilter.value?.locationFilterList

        assertEquals(locationFilterList?.elementAt(0)?.title, "Jakarta")
        assertEquals(locationFilterList?.elementAt(1)?.title, "Semarang")
        assertEquals(locationFilterList?.elementAt(2)?.title, "Other")
    }

    @Test
    fun activeFilter_priceFilter() {
        val priceFilter = productViewModelImpl.activeProductFilter.value?.priceFilter
        val allProducts = listOf(PRODUCT_1, PRODUCT_2, PRODUCT_3)

        assertEquals(priceFilter?.minimumPrice, allProducts.minBy { it.priceInt }?.priceInt )
        assertEquals(priceFilter?.maximumPrice, allProducts.maxBy { it.priceInt }?.priceInt)
    }

    @Test
    fun applyFilter_withCity() {
        productViewModelImpl.applyFilter(
                PRODUCT_1.shop.city,
                PRODUCT_1.priceInt,
                PRODUCT_1.priceInt
        )

        assertEquals(productViewModelImpl.activeProducts.value?.size, 1)
        assertEquals(productViewModelImpl.activeProducts.value?.firstOrNull(), PRODUCT_1)
    }

    @Test
    fun applyFilter_other() {
        productViewModelImpl.applyFilter(
                "Other",
                0,
                Int.MAX_VALUE
        )

        assertEquals(productViewModelImpl.activeProducts.value?.size, 0)
    }

    private fun createViewModel(): ProductViewModelImpl {
        return ProductViewModelImpl(
                TrampolineSchedulerProvider(),
                productLoader = object: ProductLoader{
                    override fun load(rawResId: Int): Observable<List<Product>> {
                        return Observable.just(listOf(PRODUCT_1, PRODUCT_2, PRODUCT_3))
                    }
                },
                repository = ProductRepository()
        )
    }

    companion object {
        val PRODUCT_1 = Product(
                id = 1,
                name = "Product 1",
                imageUrl = "",
                priceInt = 1000,
                discountPercentage = 0,
                slashedPriceInt = 0,
                shop = Shop(
                        id = 1,
                        name = "Ace Hardware",
                        city = "Jakarta"
                )
        )
        val PRODUCT_2 = Product(
                id = 2,
                name = "Product 2",
                imageUrl = "",
                priceInt = 20_000,
                discountPercentage = 0,
                slashedPriceInt = 0,
                shop = Shop(
                        id = 2,
                        name = "Informa",
                        city = "Semarang"
                )
        )
        val PRODUCT_3 = Product(
                id = 3,
                name = "Product 3",
                imageUrl = "",
                priceInt = 30_000,
                discountPercentage = 0,
                slashedPriceInt = 0,
                shop = Shop(
                        id = 3,
                        name = "Hero",
                        city = "Jakarta"
                )
        )
    }
}