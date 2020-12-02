package com.tokopedia.filter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.tokopedia.filter.R
import com.tokopedia.filter.model.ProductFilter
import com.tokopedia.filter.repo.ProductRepository
import com.tokopedia.filter.viewmodel.ProductViewModel
import com.tokopedia.filter.viewmodel.ProductViewModelFactory
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterDialogFragment : BottomSheetDialogFragment() {

    private val productViewModel: ProductViewModel by activityViewModels(){ ProductViewModelFactory(ProductRepository()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel.activeProductFilter.observe(
                viewLifecycleOwner,
                Observer {
                    syncFilter(it)
                }
        )
    }

    private fun syncFilter(filter: ProductFilter?) {
        // Location
        fragment_filter_location_group.removeAllViews()
        filter?.locationFilterList?.forEach { item ->
            val newChip = layoutInflater.inflate(R.layout.component_chip, null) as Chip
            newChip.text = item.title
            newChip.isChecked = item.checked
            fragment_filter_location_group.addView(newChip)
        }
        fragment_filter_location_group.isSingleSelection = true

        //price
        filter?.priceFilter?.also { priceFilter ->
            fragment_filter_price_slider.apply {
                valueFrom = priceFilter.minimumPrice.toFloat()
                valueTo = priceFilter.maximumPrice.toFloat()
                values = listOf(
                        priceFilter.selectedMinimumPrice.toFloat(),
                        priceFilter.selectedMaximumPrice.toFloat()
                )
                stepSize = 1.0f
            }
        }

        fragment_filter_submit.setOnClickListener {
            var selectedLocation: String? = null
            for (index in 0 until fragment_filter_location_group.childCount) {
                val chip = fragment_filter_location_group.getChildAt(index) as Chip
                if (chip.isChecked) {
                    selectedLocation = chip.text.toString()
                }
            }
            productViewModel.applyFilter(
                    selectedLocation = selectedLocation,
                    selectedMinimumPrice = fragment_filter_price_slider.values.first().toInt(),
                    selectedMaximumPrice = fragment_filter_price_slider.values.last().toInt()
            )
            dismiss()
        }
    }
}