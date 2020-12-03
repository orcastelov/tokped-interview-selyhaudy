package com.tokopedia.filter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.tokopedia.filter.R
import com.tokopedia.filter.adapter.ProductGridAdapter
import com.tokopedia.filter.datasource.ProductLoaderImpl
import com.tokopedia.filter.model.Product
import com.tokopedia.filter.repo.ProductRepository
import com.tokopedia.filter.viewmodel.ProductViewModel
import com.tokopedia.filter.viewmodel.ProductViewModelImpl
import com.tokopedia.filter.viewmodel.ProductViewModelFactory
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListFragment : Fragment() {

    private val productViewModel: ProductViewModel by activityViewModels<ProductViewModelImpl>() {
        ProductViewModelFactory(
                ProductLoaderImpl(context!!),
                ProductRepository()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_list, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel.activeProducts.observe(
                viewLifecycleOwner,
                Observer {
                    handleProducts(it)
                }
        )
    }

    private fun handleProducts(productList: List<Product>) {
        val context = context ?: return
        fragment_product_list_grid.adapter = ProductGridAdapter(
                context,
                productList
        )
    }
}