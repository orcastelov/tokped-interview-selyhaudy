package com.tokopedia.filter.datasource

import androidx.annotation.RawRes
import com.tokopedia.filter.model.Product
import io.reactivex.Observable

interface ProductLoader {
    fun load(@RawRes rawResId: Int): Observable<List<Product>>
}