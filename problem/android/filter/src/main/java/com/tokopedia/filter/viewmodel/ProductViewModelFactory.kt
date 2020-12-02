package com.tokopedia.filter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.filter.repo.ProductRepository

class ProductViewModelFactory (private val productRepository: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProductRepository::class.java).newInstance(productRepository)
    }
}