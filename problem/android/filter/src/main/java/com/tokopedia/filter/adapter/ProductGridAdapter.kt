package com.tokopedia.filter.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.tokopedia.filter.model.Product
import com.tokopedia.filter.view.ProductCardView


class ProductGridAdapter(
        private val context: Context,
        private val activeProducts: List<Product>) : BaseAdapter() {

    override fun getCount(): Int {
        return activeProducts.size
    }

    override fun getItem(i: Int): Product {
        return activeProducts[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        return ProductCardView(activeProducts[i], context)
    }
}