package com.tokopedia.filter.view

import android.content.Context
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.tokopedia.filter.R
import com.tokopedia.filter.model.Product
import kotlinx.android.synthetic.main.product_card.view.*

class ProductCardView(
        product: Product,
        context: Context
) : FrameLayout(context){

    init {
        val view = inflate(context, R.layout.product_card, this)
        view.product_card_title.text = product.name
        view.product_card_price.text = product.priceInt.toString()
        view.product_card_city.text = product.shop.city
        view.product_card_shop.text = product.shop.name
        isClickable = true

        Glide.with(context)
                .load(product.imageUrl)
                .into(view.product_card_image)
    }
}