package com.tokopedia.filter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.filter.datasource.ProductLoader
import com.tokopedia.filter.datasource.ProductLoaderImpl
import com.tokopedia.filter.repo.ProductRepository
import com.tokopedia.filter.scheduler.BaseSchedulerProvider
import com.tokopedia.filter.scheduler.SchedulerProvider

class ProductViewModelFactory (
        private val productLoader: ProductLoaderImpl,
        private val productRepository: ProductRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
                .getConstructor(BaseSchedulerProvider::class.java, ProductLoader::class.java, ProductRepository::class.java)
                .newInstance(SchedulerProvider(), productLoader, productRepository)
    }
}