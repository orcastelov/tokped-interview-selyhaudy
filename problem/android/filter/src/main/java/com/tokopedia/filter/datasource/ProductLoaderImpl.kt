package com.tokopedia.filter.datasource

import android.content.Context
import androidx.annotation.RawRes
import com.tokopedia.filter.model.Product
import com.tokopedia.filter.model.Shop
import io.reactivex.Observable
import org.json.JSONObject
import java.nio.charset.Charset

class ProductLoaderImpl(
        private val context: Context
): ProductLoader {
    override fun load(@RawRes rawResId: Int): Observable<List<Product>> {
        return Observable.create {
            val products = mutableListOf<Product>()
            val productJsonText = readProductJsonFile(context, rawResId)

            if (productJsonText != null) {
                val productJson = JSONObject(productJsonText)
                val productArray = productJson.getJSONObject("data").getJSONArray("products")

                for (i in 0 until productArray.length()) {
                    val product = productArray.getJSONObject(i)
                    val shop = product.getJSONObject("shop")
                    products.add(
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

            it.onNext(products)
            it.onComplete()
        }
    }

    private fun readProductJsonFile(context: Context, @RawRes rawResId: Int): String? {
        return context.resources.openRawResource(rawResId).use {
            val size: Int = it.available()
            val buffer = ByteArray(size)
            it.read(buffer)
            String(buffer, Charset.forName("UTF-8"))
        }
    }
}