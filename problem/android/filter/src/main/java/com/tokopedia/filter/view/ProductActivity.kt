package com.tokopedia.filter.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.tokopedia.filter.R
import kotlinx.android.synthetic.main.activity_product.*


class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        if (savedInstanceState == null) {
            val newFragment = ProductListFragment()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.add(R.id.product_list_container, newFragment).commit()
        }

        filter_button.setOnClickListener {
            FilterDialogFragment().show(supportFragmentManager, "filter")
        }
    }
}