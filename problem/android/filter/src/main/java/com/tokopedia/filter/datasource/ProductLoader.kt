package com.tokopedia.filter.datasource

import android.content.Context
import androidx.annotation.RawRes
import com.tokopedia.filter.model.Product
import com.tokopedia.filter.model.Shop
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class ProductLoader(
        val context: Context
) {
    fun load(@RawRes rawResId: Int): List<Product> {
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

        return products
    }

    private fun readProductJsonFile(context: Context, @RawRes rawResId: Int): String? {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.resources.openRawResource(rawResId)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        } finally {
            inputStream?.close() // todo "use" kotlin
        }
    }
}