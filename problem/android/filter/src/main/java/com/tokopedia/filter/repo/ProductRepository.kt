package com.tokopedia.filter.repo

import androidx.annotation.RawRes
import com.tokopedia.filter.datasource.ProductLoader
import com.tokopedia.filter.model.Product

class ProductRepository() {
    fun loadProduct(productLoader: ProductLoader, @RawRes rawResId: Int): List<Product> {
        return productLoader.load(rawResId)
    }
}